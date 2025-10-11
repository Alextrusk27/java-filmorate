package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository repository;

    public Collection<Mpa> getAll() {
        log.info("Получение списка всех MPA");
        Collection<Mpa> result = repository.getAll();
        return new ArrayList<>(result);
    }

    public Mpa get(long id) {
        log.info("Запрос на поиск MPA с ID {}", id);
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException("MPA с ID: " + id + " не найден"));
    }
}