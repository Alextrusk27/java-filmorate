package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    public static final long MAX_MPA = 5;
    public static final long MAX_GENRE = 6;

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());

        if (request.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года;");
        } else {
            film.setReleaseDate(request.getReleaseDate());
        }

        film.setDuration(request.getDuration());

        if (request.getMpa() != null) {
            if (request.getMpa().getId() > 0 && request.getMpa().getId() <= MAX_MPA) {
                film.setMpa(request.getMpa());
            } else {
                throw new NotFoundException("Неверный MPA ID. Может быть от 1 до " + MAX_MPA);
            }
        }
        if (request.getGenres() != null) {
            request.getGenres().forEach(genre -> {
                if (genre.getId() < 0 || genre.getId() > MAX_GENRE) {
                    throw new NotFoundException("Неверный Genre ID. Может быть от 1 до " + MAX_GENRE);
                }
            });
            film.setGenres(request.getGenres());
        }
        return film;
    }

    public static Film updateFilmFields(UpdateFilmRequest request) {
        Film film = new Film();
        if (request.hasName()) {
            film.setName(request.getName());
        }
        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }
        if (request.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года;");
        } else {
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }
        if (request.getMpa() != null) {
            if (request.getMpa().getId() > 0 && request.getMpa().getId() <= MAX_MPA) {
                film.setMpa(request.getMpa());
            } else {
                throw new NotFoundException("Неверный MPA ID. Может быть от 1 до " + MAX_MPA);
            }
        }
        if (request.getGenres() != null) {
            request.getGenres().forEach(genre -> {
                if (genre.getId() < 0 || genre.getId() > MAX_GENRE) {
                    throw new NotFoundException("Неверный Genre ID. Может быть от 1 до " + MAX_GENRE);
                }
            });
            film.setGenres(request.getGenres());
        }
        film.setId(request.getId());
        return film;
    }
}