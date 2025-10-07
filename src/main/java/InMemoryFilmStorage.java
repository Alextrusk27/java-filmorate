//package ru.yandex.practicum.filmorate.repository;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
//import ru.yandex.practicum.filmorate.exceptions.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.time.LocalDate;
//import java.util.*;
//
//@Slf4j
//@Component
//public class InMemoryFilmStorage implements UserRepository<Film> {
//
//    public static final int FILM_DESCRIPTION_MAX_LENGTH = 200;
//    public static final LocalDate FILM_MIN_RELEASE_DATE = LocalDate.parse("1895-12-28");
//
//    private final Map<Long, Film> films = new HashMap<>();
//
//    @Override
//    public Film save(Film film) {
//        filmValidator(film);
//        film.setId(getNextId());
//        films.put(film.getId(), film);
//        return film;
//    }
//
//    @Override
//    public Film update(Film film) {
//        if (film.getId() == null) {
//            throw new ValidationException("ID фильма не указан");
//        }
//        if (!films.containsKey(film.getId())) {
//            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден");
//        }
//        filmValidator(film);
//        Film oldFilm = films.get(film.getId());
//        oldFilm.setName(film.getName());
//
//        Optional.ofNullable(film.getDescription())
//                .filter(d -> !d.isBlank())
//                .ifPresent(oldFilm::setDescription);
//
//        if (film.getReleaseDate() != null) {
//            oldFilm.setReleaseDate(film.getReleaseDate());
//        }
//        if (film.getDuration() != null) {
//            oldFilm.setDuration(film.getDuration());
//        }
//        return oldFilm;
//    }
//
//    @Override
//    public Collection<Film> getAll() {
//        return new ArrayList<>(films.values());
//    }
//
//    @Override
//    public Optional<Film> get(long id) {
//        if (films.containsKey(id)) {
//            return Optional.ofNullable(films.get(id));
//        } else {
//            throw new NotFoundException("Фильм с ID " + id + " не найден");
//        }
//    }
//
//    private void filmValidator(Film film) {
//        if (film.getName() == null || film.getName().isBlank()) {
//            throw new ValidationException("Название фильма не указано");
//        }
//        if (film.getDescription() != null) {
//            if (film.getDescription().length() >= FILM_DESCRIPTION_MAX_LENGTH)
//                throw new ValidationException("Описание фильма не должно превышать 200 символов");
//        }
//        if (film.getReleaseDate() != null) {
//            if (film.getReleaseDate().isBefore(FILM_MIN_RELEASE_DATE))
//                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
//        }
//        if (film.getDuration() != null) {
//            if (film.getDuration() <= 0) {
//                throw new ValidationException("Продолжительность фильма должна быть больше 0");
//            }
//        }
//    }
//
//    private long getNextId() {
//        long currentMaxId = films.keySet()
//                .stream()
//                .mapToLong(id -> id)
//                .max()
//                .orElse(0);
//        ++currentMaxId;
//        return currentMaxId;
//    }
//}