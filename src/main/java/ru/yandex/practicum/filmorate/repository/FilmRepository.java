package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmRepository {

    Film save(Film film);

    Film update(Film film);

    Collection<Film> getFilms(long count);

    Film get(long id);

    void setLike(long user_id, long film_id);

    void removeLike(long user_id, long film_id);
}
