package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage<T> {

    T create(T entity);

    T update(T entity);

    Collection<T> getAll();

    T get(long id);
}