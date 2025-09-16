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
        userValidator(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
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
        return oldUser;
    }

    @Override
    public Collection<User> getAll() {
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
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0L);
        ++currentMaxId;
        return currentMaxId;
    }
}