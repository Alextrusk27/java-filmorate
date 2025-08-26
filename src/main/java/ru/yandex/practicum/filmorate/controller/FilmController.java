package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    public static final int FILM_DESCRIPTION_MAX_LENGTH = 200;
    public static final LocalDate FILM_MIN_RELEASE_DATE = LocalDate.parse("1895-12-28");

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Запрос на создание фильма");
        filmValidator(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Создан фильм {} ID {}", film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        if (newFilm.getId() == null)
            throw new ValidationException("ID фильма не указан");

        if (!films.containsKey(newFilm.getId()))
            throw new NotFoundException("Фильм с ID " + newFilm.getId() + " не найден");

        filmValidator(newFilm);
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());

        Optional.ofNullable(newFilm.getDescription())
                .filter(d -> !d.isBlank())
                .ifPresent(oldFilm::setDescription);

        if (newFilm.getReleaseDate() != null)
            oldFilm.setReleaseDate(newFilm.getReleaseDate());

        if (newFilm.getDuration() != null)
            oldFilm.setDuration(newFilm.getDuration());

        log.info("Изменение фильма {} ID {} завершено", oldFilm.getName(), oldFilm.getId());
        return oldFilm;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получение списка фильмов");
        return films.values();
    }

    public void filmValidator(Film film) {
        log.debug("Валидация фильма");
        if (film.getName() == null || film.getName().isBlank())
            throw new ValidationException("Название фильма не указано");

        if (film.getDescription() != null)
            if (film.getDescription().length() >= FILM_DESCRIPTION_MAX_LENGTH)
                throw new ValidationException("Описание фильма не должно превышать 200 символов");

        if (film.getReleaseDate() != null)
            if (film.getReleaseDate().isBefore(FILM_MIN_RELEASE_DATE))
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");

        if (film.getDuration() != null)
            if (film.getDuration() <= 0)
                throw new ValidationException("Продолжительность фильма должна быть больше 0");
        log.debug("Валидация фильма завершена");
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        ++currentMaxId;
        log.trace("Сгенерирован Film ID {}", currentMaxId);
        return currentMaxId;
    }
}
