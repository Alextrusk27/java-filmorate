//package ru.yandex.practicum.filmorate;
//
//import org.junit.jupiter.api.*;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.yandex.practicum.filmorate.exceptions.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
//import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.time.LocalDate;
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class FilmorateApplicationTests {
//
//    private Film film;
//    private User user;
//
//    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
//    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();
//
//    private Method filmValidator;
//    private Method userValidator;
//
//    @BeforeAll
//    public void initValidator() throws NoSuchMethodException {
//        filmValidator = InMemoryFilmStorage.class.getDeclaredMethod("filmValidator", Film.class);
//        filmValidator.setAccessible(true);
//        userValidator = InMemoryUserStorage.class.getDeclaredMethod("userValidator", User.class);
//        userValidator.setAccessible(true);
//    }
//
//    @BeforeEach
//    void setup() {
//        film = Film.builder()
//                .name("Храброе сердце")
//                .description("История легендарного национального героя Уильяма Уоллеса, посвятившего себя борьбе " +
//                        "с англичанами при короле Эдварде Длинноногом")
//                .releaseDate(LocalDate.parse("1995-05-18"))
//                .duration(178L)
//                .build();
//        user = User.builder()
//                .email("MelGibsonFun116@yahoo.com")
//                .login("MikesFilms")
//                .name("Mike")
//                .birthday(LocalDate.parse("1993-03-27"))
//                .build();
//    }
//
//    @Test
//    void basicFilmValidation() {
//        Assertions.assertDoesNotThrow(() -> filmValidator.invoke(filmStorage, film),
//                "Валидатор фильмов не должен вернуть исключение");
//    }
//
//    @Test
//    void filmNameCannotBeNull() {
//        film.setName(null);
//        Assertions.assertEquals("Название фильма не указано", getReason(filmValidator, filmStorage, film));
//    }
//
//    @Test
//    void filmNameCannotBeBlank() {
//        film.setName(" ");
//        Assertions.assertEquals("Название фильма не указано", getReason(filmValidator, filmStorage, film));
//    }
//
//    @Test
//    void filmDescriptionCannotBeMoreThan200() {
//        film.setDescription("Действие фильма начинается в 1280 году в Шотландии. Это история легендарного " +
//                "национального героя Уильяма Уоллеса, посвятившего себя борьбе с англичанами при короле Эдварде " +
//                "Длинноногом. Он рано лишился отца, погибшего от рук англичан, и его забрал к себе дядя Оргайл, " +
//                "который дал ему хорошее образование в Европе.");
//        Assertions.assertEquals("Описание фильма не должно превышать 200 символов",
//                getReason(filmValidator, filmStorage, film));
//    }
//
//    @Test
//    void filmReleaseDateCannotBeBefore28Dec1985() {
//        film.setReleaseDate(LocalDate.parse("1805-05-18"));
//        Assertions.assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года",
//                getReason(filmValidator, filmStorage, film));
//    }
//
//    @Test
//    void filmDurationCannotBeLessThanZero() {
//        film.setDuration(-5L);
//        Assertions.assertEquals("Продолжительность фильма должна быть больше 0",
//                getReason(filmValidator, filmStorage, film));
//    }
//
//    @Test
//    void basicUserValidation() {
//        Assertions.assertDoesNotThrow(() -> userValidator.invoke(userStorage, user),
//                "Валидатор пользователей не должен вернуть исключение");
//    }
//
//    @Test
//    void userLoginCannotBeNull() {
//        user.setLogin(null);
//        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы",
//                getReason(userValidator, userStorage, user));
//    }
//
//    @Test
//    void userLoginCannotBeBlank() {
//        user.setLogin(" ");
//        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы",
//                getReason(userValidator, userStorage, user));
//    }
//
//    @Test
//    void userEmailCannotBeNull() {
//        user.setEmail(null);
//        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
//                getReason(userValidator, userStorage, user));
//    }
//
//
//    @Test
//    void userEmailCannotBeBlank() {
//        user.setEmail(" ");
//        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
//                getReason(userValidator, userStorage, user));
//    }
//
//    @Test
//    void userEmailMustIncludeAt() {
//        user.setEmail("MelGibsonFun116yahoo.com");
//        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
//                getReason(userValidator, userStorage, user));
//    }
//
//    @Test
//    void userBirthdayCannotBeInFuture() {
//        user.setBirthday(LocalDate.now().plusDays(1));
//        Assertions.assertEquals("Дата рождения не может быть в будущем",
//                getReason(userValidator, userStorage, user));
//    }
//
//    private <T, E> String getReason(Method method, T storage, E entity) {
//        var exception = Assertions.assertThrows(InvocationTargetException.class, () ->
//                method.invoke(storage, entity));
//        Throwable cause = exception.getTargetException();
//        Assertions.assertInstanceOf(ValidationException.class, cause);
//        return cause.getMessage();
//    }
//}