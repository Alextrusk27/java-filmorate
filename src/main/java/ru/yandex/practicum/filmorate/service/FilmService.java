package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    public static final long DEFAULT_FILM_COUNT = 0;

    private final FilmRepository repository;

    public Film create(NewFilmRequest request) {
        Film result = repository.save(FilmMapper.mapToFilm(request));
        log.info("Создан фильм {} ID {}", result.getName(), result.getId());
        return result;
    }

    public Film update(UpdateFilmRequest request) {
        Film result = repository.update(FilmMapper.updateFilmFields(request));
        log.info("Изменение фильма {} ID {} завершено", result.getName(), result.getId());
        return result;
    }

    public Collection<Film> getAll() {
        log.info("Получение списка фильмов");
        return repository.getFilms(DEFAULT_FILM_COUNT);
    }

    public Film get(long id) {
        log.info("Запрос на поиск фильма с ID {}", id);
        return repository.get(id);
    }

    public void setLike(long id, long userId) {
        repository.setLike(id, userId);
        log.info("Поставлен лайк фильму с ID {} пользователем с ID {}", id, userId);
    }

    public void removeLike(long id, long userId) {
        repository.removeLike(id, userId);
        log.info("Удален лайк с фильма с ID {} пользователем с ID {}", id, userId);
    }

    public Collection<Film> getTopFilms(long count) {
        log.info("Запрос на топ {} попурных фильмов", count);
        return repository.getFilms(count);
    }
}