package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final Storage<User> userStorage;

    public User createUser(User user) {
        User result = userStorage.create(user);
        log.info("Создан пользователь {} логин {} ID {}", result.getName(), result.getLogin(), result.getId());
        return result;
    }

    public User updateUser(User user) {
        User result = userStorage.update(user);
        log.info("Изменение данных пользователя ID {} завершено", result.getId());
        return result;
    }

    public Collection<User> getAllUsers() {
        log.info("Запрос на получение списка пользователей");
        return userStorage.getAll();
    }

    public User getUserById(long id) {
        log.info("Запрос на поиск пользователя с ID {}", id);
        return userStorage.get(id);
    }

    public void addToFriends(long id, long friendId) {
        log.info("Запрос на добавление в друзья пользователей с ID {} и ID {}", id, friendId);
        User firstUser = getUserById(id);
        User secondUser = getUserById(friendId);
        firstUser.getFriends().add(friendId);
        secondUser.getFriends().add(id);
    }

    public void deleteFriend(long id, long friendId) {
        log.info("Запрос на удаление из друзей пользователей с ID {} и ID {}", id, friendId);
        Set<Long> firstUserFriends = getUserById(id).getFriends();
        Set<Long> secondUserFriends = getUserById(friendId).getFriends();
        firstUserFriends.remove(friendId);
        secondUserFriends.remove(id);
    }

    public Collection<User> getFriends(long id) {
        log.info("Запрос на получение списка друзей пользователя с ID {}", id);
        return getUserById(id).getFriends().stream()
                .map(this::getUserById)
                .toList();
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        log.info("Запрос на получение общих друзей пользователей с ID {} и ID {}", id, otherId);
        Set<Long> firstUserFriends = getUserById(id).getFriends();
        Set<Long> secondUserFriends = getUserById(otherId).getFriends();

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(this::getUserById)
                .toList();
    }
}