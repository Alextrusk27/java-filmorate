package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.rowMapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.rowMapper.GenreRowMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.FilmService.GET_ALL_FILMS;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper mapper;

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());
        jdbc.update("""
                INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
                VALUES (:name, :description, :release_date, :duration , :mpa_id)
                """, params, keyHolder, new String[]{"id"});
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        Mpa mpa = mpaRepository.get(film.getMpa().getId()).orElseThrow();
        film.setMpa(mpa);
        return updateGenres(film);
    }

    @Override
    public Film update(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", film.getId());
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());
        int rowsAffected = jdbc.update("""
                UPDATE FILMS
                SET name = :name,
                description = :description,
                release_date = :release_date,
                duration = :duration,
                mpa_id = :mpa_id
                WHERE id = :id
                """, params);
        if (rowsAffected == 0) {
            throw new NotFoundException("Фильм с ID: " + film.getId() + " не найден");
        }
        Mpa mpa = mpaRepository.get(film.getMpa().getId()).orElseThrow();
        film.setMpa(mpa);
        return updateGenres(film);
    }

    @Override
    public Collection<Film> getFilms(long count) {
        // получаем все фильмы без жанров
        List<Film> allFilms = new ArrayList<>();
        if (count == GET_ALL_FILMS) {
            allFilms = jdbc.query("""
                        SELECT f.id,
                        f.name,
                        f.description,
                        f.release_date,
                        f.duration,
                        f.mpa_id,
                        m.name AS mpa_name
                    FROM FILMS AS f
                    JOIN MPA AS m ON f.mpa_id = m.id
                    """, mapper);
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource("count", count);
            allFilms = jdbc.query("""
                    SELECT
                      f.id,
                      f.name,
                      f.description,
                      f.release_date,
                      f.duration,
                      f.mpa_id,
                      m.name AS mpa_name
                    FROM FILMS AS f
                    JOIN MPA AS m ON f.mpa_id = m.id
                    LEFT JOIN LIKES AS l ON f.id = l.film_id
                    GROUP BY
                          f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name
                    ORDER BY COUNT(l.user_id) DESC
                    LIMIT :count;
                    """, params, mapper);
        }
        Map<Long, Film> filmsTable = allFilms.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        // получаем связи фильм-жанры
        Map<Long, Set<Long>> connections = new HashMap<>();
        jdbc.query("SELECT film_id, genre_id FROM FILM_GENRES", rs -> {
            long filmId = rs.getLong("film_id");
            long genreId = rs.getLong("genre_id");
            connections.computeIfAbsent(filmId, k -> new HashSet<>())
                    .add(genreId);
        });

        // получаем таблицу жанров
        Collection<Genre> genresList = genreRepository.getAll();
        Map<Long, Genre> genres = genresList.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity(), (a, b) -> a, HashMap::new));

        // добавляем жанры и собираем список фильмов
        Collection<Film> result = new LinkedHashSet<>(filmsTable.values());
        connections.entrySet().stream()
                .forEach(entry -> {
                            Film film = filmsTable.get(entry.getKey());
                            if (film != null) {
                                film.setGenres(new HashSet<>());
                                entry.getValue().forEach(genreId -> {
                                    if (genreId != null) {
                                        Genre genre = genres.get(genreId);
                                        film.getGenres().add(genre);
                                    }
                                });
                            }
                        }
                );
        return result;
    }

    @Override
    public Film get(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            Film result = jdbc.queryForObject("""
                    SELECT f.id,
                        f.name,
                        f.description,
                        f.release_date,
                        f.duration,
                        f.mpa_id,
                        m.name AS mpa_name
                    FROM FILMS AS f
                    JOIN MPA AS m ON f.mpa_id = m.id
                    WHERE f.id = :id
                    GROUP BY f.id
                    """, params, mapper);
            if (result != null) {
                return saveGenres(result);
            } else {
                throw new NotFoundException("Фильм с ID: " + id + " не найден");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с ID: " + id + " не найден");
        }
    }

    @Override
    public void setLike(long film_id, long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", film_id);
        params.addValue("user_id", filmId);
        jdbc.update("""
                MERGE INTO LIKES (user_id, film_id)
                KEY (user_id, film_id)
                VALUES (:user_id, :film_id)
                """, params);
    }

    @Override
    public void removeLike(long film_id, long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", film_id);
        params.addValue("user_id", filmId);
        jdbc.update("""
                DELETE FROM LIKES
                WHERE USER_ID = :user_id AND film_id = :film_id;
                """, params);
    }

    private Film updateGenres(Film film) {
        if (film.getGenres() != null) {
            List<Map<String, Long>> batchValues = film.getGenres().stream()
                    .map(genre -> Map.of(
                            "film_id", film.getId(),
                            "genre_id", genre.getId()
                    ))
                    .toList();

            MapSqlParameterSource params = new MapSqlParameterSource("film_id", film.getId());

            // удаляем все жанры фильма в бд
            jdbc.update("""
                    DELETE FROM FILM_GENRES
                    WHERE film_id = :film_id
                    """, params);

            // перезаписываем жанры фильма в бд
            jdbc.batchUpdate("""
                    INSERT INTO FILM_GENRES (film_id, genre_id)
                    VALUES (:film_id, :genre_id)
                    """, SqlParameterSourceUtils.createBatch(batchValues));
            return saveGenres(film);
        }
        return saveGenres(film);
    }

    private Film saveGenres(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource("film_id", film.getId());
        GenreRowMapper genreRowMapper = new GenreRowMapper();
        // создаем поле с жанрами для дто
        film.setGenres(new LinkedHashSet<>(jdbc.query("""
                SELECT *
                FROM GENRES
                WHERE id in (
                SELECT genre_id
                FROM FILM_GENRES
                WHERE film_id = :film_id)
                ORDER BY id;
                """, params, genreRowMapper)));
        return film;
    }
}