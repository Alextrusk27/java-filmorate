package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User mapToUser(NewUserRequest request) {
        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setBirthday(request.getBirthday());

        if (request.hasName()) {
            user.setName(request.getName());
        } else {
            user.setName(request.getLogin());
        }
        return user;
    }

    public static User updateUserFields(UpdateUserRequest request) {
        User user = new User();
        if (request.hasLogin()) {
            user.setLogin(request.getLogin());
        }
        if (request.hasName()) {
            user.setName(request.getName());
        }
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }
        user.setId(request.getId());
        return user;
    }
}
