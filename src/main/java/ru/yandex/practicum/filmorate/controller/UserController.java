package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {
        log.debug("Запрос на создание пользователя");
        userValidator(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя заменено на логин");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан пользователь {} логин {} ID {}", user.getName(), user.getLogin(), user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("ID пользователя не указан");
        }
        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException("пользователь с ID " + newUser.getId() + " не найден");
        }
        userValidator(newUser);
        User oldUser = users.get(newUser.getId());
        Optional.ofNullable(newUser.getName())
                .filter(s -> !s.isBlank())
                .ifPresent(oldUser::setName);
        oldUser.setLogin(newUser.getLogin());
        oldUser.setEmail(newUser.getEmail());
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
        log.info("Изменение данных пользователя {} ID {} завершено", oldUser.getName(), oldUser.getId());
        return oldUser;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получение списка фильмов");
        return new ArrayList<>(users.values());
    }

    public void userValidator(User user) {
        log.debug("Валидация пользователя");
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().codePoints()
                .anyMatch(Character::isWhitespace)) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getBirthday() != null) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
        }
        log.debug("Валидация пользователя завершена");
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        ++currentMaxId;
        log.trace("Сгенерирован User ID {}", currentMaxId);
        return currentMaxId;
    }
}

