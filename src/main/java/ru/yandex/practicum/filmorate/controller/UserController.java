package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.users.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.users.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody final NewUserRequest request) {
        return userService.create(request);
    }

    @PutMapping
    public User update(@Valid @RequestBody final UpdateUserRequest request) {
        return userService.update(request);
    }

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable final long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(
            @PathVariable final long id,
            @PathVariable final long friendId
    ) {
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable final long id,
            @PathVariable final long friendId
    ) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable final long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable final long id,
            @PathVariable final long otherId
    ) {
        return userService.getCommonFriends(id, otherId);
    }
}