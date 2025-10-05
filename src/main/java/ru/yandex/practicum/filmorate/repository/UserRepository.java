package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    User update(User user);

    Collection<User> getAll();

    Optional<User> get(long id);

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);

    Collection<User> getFriends(long id);

    Collection<User> getCommonFriends(long id, long friendId);
}