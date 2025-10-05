package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User create(NewUserRequest request) {
        User result = repository.save(UserMapper.mapToUser(request));
        log.info("Создан пользователь {} логин {} ID {}", result.getName(), result.getLogin(), result.getId());
        return result;
    }

    public User update(UpdateUserRequest request) {
        User result = repository.update(UserMapper.updateUserFields(request));
        log.info("Изменение данных пользователя ID {} завершено", result.getId());
        return result;
    }

    public Collection<User> getAll() {
        log.info("Получение списка всех пользователей");
        return repository.getAll();
    }

    public User getUserById(long id) {
        log.info("Запрос на поиск пользователя с ID {}", id);
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + id + " не найден"));
    }

    public void addToFriends(long id, long friendId) {
        log.info("Запрос на добавление в друзья пользователей с ID {} и ID {}", id, friendId);
        checkUsersExist(id, friendId);
        repository.addFriend(id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        log.info("Запрос на удаление из друзей пользователей с ID {} и ID {}", id, friendId);
        checkUsersExist(id, friendId);
        repository.removeFriend(id, friendId);
    }

    public Collection<User> getFriends(long id) {
        log.info("Запрос на получение списка друзей пользователя с ID {}", id);
        checkUsersExist(id);
        return repository.getFriends(id);
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        log.info("Запрос на получение списка общих друзей пользователей с ID {} и ID {}", id, friendId);
        checkUsersExist(id, friendId);
        return repository.getCommonFriends(id, friendId);
    }

    private void checkUsersExist(long... ids) {
        Arrays.stream(ids).forEach(this::getUserById);
    }
}