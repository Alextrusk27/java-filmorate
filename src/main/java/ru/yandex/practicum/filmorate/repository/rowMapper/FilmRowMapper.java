package ru.yandex.practicum.filmorate.repository.rowMapper;

import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getLong(("duration")));
        Timestamp date = rs.getTimestamp("release_date");
        film.setReleaseDate(date.toLocalDateTime().toLocalDate());

        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("mpa_id"));
        mpa.setName(rs.getString("mpa_name"));
        film.setMpa(mpa);

        return film;
    }
}