package ru.yandex.practicum.filmorate;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;
import ru.yandex.practicum.filmorate.repository.rowMapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.repository.rowMapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.repository.rowMapper.UserRowMapper;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Random;

@JdbcTest
@Import({JdbcFilmRepository.class, FilmRowMapper.class, JdbcMpaRepository.class, MpaRowMapper.class,
        JdbcGenreRepository.class, GenreRowMapper.class, JdbcUserRepository.class, UserRowMapper.class})
public class JdbcBaseRepositoryTest {
    protected static final long FIRST_FILM_ID = 1L;
    protected static final long SECOND_FILM_ID = 2L;
    protected static final long THIRD_FILM_ID = 3L;
    protected static final long NEW_FILM_ID = 4L;
    protected static final long FIRST_USER_ID = 1L;
    protected static final long SECOND_USER_ID = 2L;
    protected static final long THIRD_USER_ID = 3L;
    protected static final long FORTH_USER_ID = 4L;

    protected static Film getFilm(long id) {
        Film film = new Film();
        switch (Math.toIntExact(id)) {
            case 1 -> {
                film.setId(FIRST_FILM_ID);
                film.setName("title_1");
                film.setDescription("description_1");
                film.setReleaseDate(LocalDate.of(2001, 1, 1));
                film.setDuration(120L);
                film.setMpa(getMpa(1));
                film.setGenres(new LinkedHashSet<>());
                film.getGenres().add(getGenre(1));
                film.getGenres().add(getGenre(2));
            }
            case 2 -> {
                film.setId(SECOND_FILM_ID);
                film.setName("title_2");
                film.setDescription("description_2");
                film.setReleaseDate(LocalDate.of(2002, 2, 2));
                film.setDuration(140L);
                film.setMpa(getMpa(2));
                film.setGenres(new LinkedHashSet<>());
                film.getGenres().add(getGenre(1));
            }
            case 3 -> {
                film.setId(THIRD_FILM_ID);
                film.setName("title_3");
                film.setDescription("description_3");
                film.setReleaseDate(LocalDate.of(2003, 3, 3));
                film.setDuration(180L);
                film.setMpa(getMpa(3));
                film.setGenres(new LinkedHashSet<>());
                film.getGenres().add(getGenre(1));
                film.getGenres().add(getGenre(2));
                film.getGenres().add(getGenre(3));
            }
            case 4 -> {
                film.setId(NEW_FILM_ID);
                film.setName("title_r");
                film.setDescription("description_r");
                film.setReleaseDate(LocalDate.of(2013, 10, 10));
                film.setDuration(224L);
                film.setMpa(getMpa(5));
                film.setGenres(new LinkedHashSet<>());
                film.getGenres().add(getGenre(4));
                film.getGenres().add(getGenre(5));
            }
            default -> throw new IllegalArgumentException("Invalid id: " + id);
        }
        return film;
    }

    protected static Mpa getMpa(long id) {
        Mpa mpa = new Mpa();
        switch (Math.toIntExact(id)) {
            case 1 -> {
                mpa.setId(id);
                mpa.setName("G");
            }
            case 2 -> {
                mpa.setId(id);
                mpa.setName("PG");
            }
            case 3 -> {
                mpa.setId(id);
                mpa.setName("PG-13");
            }
            case 4 -> {
                mpa.setId(id);
                mpa.setName("R");
            }
            case 5 -> {
                mpa.setId(id);
                mpa.setName("NC-17");
            }
            default -> throw new IllegalArgumentException("Invalid id: " + id);
        }
        return mpa;
    }

    protected static Genre getGenre(long id) {
        Genre genre = new Genre();
        switch (Math.toIntExact(id)) {
            case 1 -> {
                genre.setId(id);
                genre.setName("Комедия");
            }
            case 2 -> {
                genre.setId(id);
                genre.setName("Драма");
            }
            case 3 -> {
                genre.setId(id);
                genre.setName("Мультфильм");
            }
            case 4 -> {
                genre.setId(id);
                genre.setName("Триллер");
            }
            case 5 -> {
                genre.setId(id);
                genre.setName("Документальный");
            }
            case 6 -> {
                genre.setId(id);
                genre.setName("Боевик");
            }
            default -> throw new IllegalArgumentException("Invalid id: " + id);
        }
        return genre;
    }

    protected static User getUser(long id) {
        User user = new User();
        switch (Math.toIntExact(id)) {
            case 1:
                user.setId(FIRST_USER_ID);
                user.setName("name_1");
                user.setLogin("login_1");
                user.setEmail("email-1@email.com");
                user.setBirthday(LocalDate.of(1991, 1, 1));
                break;
            case 2:
                user.setId(SECOND_USER_ID);
                user.setName("name_2");
                user.setLogin("login_2");
                user.setEmail("email-2@email.com");
                user.setBirthday(LocalDate.of(1992, 2, 2));
                break;
            case 3:
                user.setId(THIRD_USER_ID);
                user.setName("name_3");
                user.setLogin("login_3");
                user.setEmail("email-3@email.com");
                user.setBirthday(LocalDate.of(1993, 3, 3));
                break;
            case 4:
                user.setId(FORTH_USER_ID);
                user.setName("name_4");
                user.setLogin("login_4");
                user.setEmail("email-4@email.com");
                user.setBirthday(LocalDate.of(1994, 4, 4));
                break;
            default:
                throw new IllegalArgumentException("Invalid id: " + id);
        }
        return user;
    }

    private static String getRandom(int length) {
        return RandomStringUtils.random(length, 0, 120, true, false, null, new Random());
    }

    protected static User getRandomUser() {
        User user = new User();
        Random random = new Random();
        user.setName(getRandom(12));
        user.setLogin(getRandom(8));
        user.setEmail(getRandom(10) + "@email.com");
        user.setBirthday(LocalDate.of(random.nextInt(1945, 2025), random.nextInt(1, 13), random.nextInt(1, 27)));
        return user;
    }
}