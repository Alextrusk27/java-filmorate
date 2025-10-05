package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.rowMapper.MpaRowMapper;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mapper;

    @Override
    public Collection<Mpa> getAll() {
        return jdbc.query("""
                SELECT * FROM RATINGS
                ORDER BY id
                """, mapper);
    }

    @Override
    public Optional<Mpa> get(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            return Optional.ofNullable(jdbc.queryForObject("""
                    SELECT * FROM RATINGS
                    WHERE id = :id
                    """, params, mapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}