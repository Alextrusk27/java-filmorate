package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements Storage<User> {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
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

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("ID пользователя не указан");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("пользователь с ID " + user.getId() + " не найден");
        }
        userValidator(user);
        User oldUser = users.get(user.getId());
        Optional.ofNullable(user.getName())
                .filter(s -> !s.isBlank())
                .ifPresent(oldUser::setName);
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        if (user.getBirthday() != null) {
            oldUser.setBirthday(user.getBirthday());
        }
        log.info("Изменение данных пользователя {} ID {} завершено", oldUser.getName(), oldUser.getId());
        return oldUser;
    }

    @Override
    public Collection<User> getAll() {
        log.info("Получение списка пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь с ID " + id + " не найден");
        }
    }

    private void userValidator(User user) {
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

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0L);
        ++currentMaxId;
        log.trace("Сгенерирован User ID {}", currentMaxId);
        return currentMaxId;
    }
}