package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.rowMapper.GenreRowMapper;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper mapper;

    @Override
    public Collection<Genre> getAll() {
        return jdbc.query("""
                SELECT * FROM GENRES
                ORDER BY id
                """, mapper);
    }

    @Override
    public Optional<Genre> get(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            return Optional.ofNullable(jdbc.queryForObject("""
                    SELECT * FROM GENRES
                    WHERE id = :id
                    """, params, mapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}