package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository repository;

    public Collection<Genre> getAll() {
        log.info("Получение списка всех жанров");
        Collection<Genre> result = repository.getAll();
        return new ArrayList<>(result);
    }

    public Genre get(long id) {
        log.info("Запрос на поиск жанра с ID {}", id);
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException("Жанр с ID: " + id + " не найден"));
    }
}