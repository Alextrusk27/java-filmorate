package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

    private Film film;
    private User user;
    private final FilmController filmController = new FilmController();
    private final UserController userController = new UserController();

    @BeforeEach
    void setup() {
        film = Film.builder()
                .name("Храброе сердце")
                .description("История легендарного национального героя Уильяма Уоллеса, посвятившего себя борьбе " +
                        "с англичанами при короле Эдварде Длинноногом")
                .releaseDate(LocalDate.parse("1995-05-18"))
                .duration(178L)
                .build();
        user = User.builder()
                .email("MelGibsonFun116@yahoo.com")
                .login("MikesFilms")
                .name("Mike")
                .birthday(LocalDate.parse("1993-03-27"))
                .build();
    }

    @Test
    void basicFilmValidation() {
        Assertions.assertDoesNotThrow(() -> filmController.filmValidator(film),
                "Валидатор фильмов не должен вернуть исключение");
    }

    @Test
    void filmNameCannotBeNull() {
        film.setName(null);
        var exception = Assertions.assertThrows(ValidationException.class, () -> filmController.filmValidator(film));
        Assertions.assertEquals("Название фильма не указано", exception.getMessage());
    }

    @Test
    void filmNameCannotBeBlank() {
        film.setName(" ");
        var exception = Assertions.assertThrows(ValidationException.class, () -> filmController.filmValidator(film));
        Assertions.assertEquals("Название фильма не указано", exception.getMessage());
    }

    @Test
    void filmDescriptionCannotBeMoreThan200() {
        film.setDescription("Действие фильма начинается в 1280 году в Шотландии. Это история легендарного " +
                "национального героя Уильяма Уоллеса, посвятившего себя борьбе с англичанами при короле Эдварде " +
                "Длинноногом. Он рано лишился отца, погибшего от рук англичан, и его забрал к себе дядя Оргайл, " +
                "который дал ему хорошее образование в Европе.");
        var exception = Assertions.assertThrows(ValidationException.class, () -> filmController.filmValidator(film));
        Assertions.assertEquals("Описание фильма не должно превышать 200 символов", exception.getMessage());
    }

    @Test
    void filmReleaseDateCannotBeBefore28Dec1985() {
        film.setReleaseDate(LocalDate.parse("1805-05-18"));
        var exception = Assertions.assertThrows(ValidationException.class, () -> filmController.filmValidator(film));
        Assertions.assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void filmDurationCannotBeLessThanZero() {
        film.setDuration(-5L);
        var exception = Assertions.assertThrows(ValidationException.class, () -> filmController.filmValidator(film));
        Assertions.assertEquals("Продолжительность фильма должна быть больше 0", exception.getMessage());
    }

    @Test
    void basicUserValidation() {
        Assertions.assertDoesNotThrow(() -> userController.userValidator(user),
                "Валидатор пользователей не должен вернуть исключение");
    }

    @Test
    void userLoginCannotBeNull() {
        user.setLogin(null);
        var exception = Assertions.assertThrows(ValidationException.class, () -> userController.userValidator(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void userLoginCannotBeBlank() {
        user.setLogin(" ");
        var exception = Assertions.assertThrows(ValidationException.class, () -> userController.userValidator(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void userEmailCannotBeNull() {
        user.setEmail(null);
        var exception = Assertions.assertThrows(ValidationException.class, () -> userController.userValidator(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
                exception.getMessage());
    }

    @Test
    void userEmailCannotBeBlank() {
        user.setEmail(" ");
        var exception = Assertions.assertThrows(ValidationException.class, () -> userController.userValidator(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
                exception.getMessage());
    }

    @Test
    void userEmailMustIncludeAt() {
        user.setEmail("MelGibsonFun116yahoo.com");
        var exception = Assertions.assertThrows(ValidationException.class, () -> userController.userValidator(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
                exception.getMessage());
    }

    @Test
    void userBirthdayCannotBeInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        var exception = Assertions.assertThrows(ValidationException.class, () -> userController.userValidator(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }
}
