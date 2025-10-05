package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdbcFilmRepository implements FilmRepository {
    @Override
    public Film save(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return List.of();
    }

    @Override
    public Optional<Film> get(long id) {
        return Optional.empty();
    }
}
