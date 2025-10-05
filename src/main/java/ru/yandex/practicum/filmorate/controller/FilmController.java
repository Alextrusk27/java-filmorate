//package ru.yandex.practicum.filmorate.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.service.FilmService;
//import ru.yandex.practicum.filmorate.service.UserService;
//
//@RestController
//@AllArgsConstructor
//@RequestMapping("/films")
//public class FilmController {
////
////    private final FilmService filmService;
////    private final UserService userService;
////
////    @PostMapping
////    public Film create(@RequestBody Film film) {
////        return filmService.createFilm(film);
////    }
////
////    @PutMapping
////    public Film update(@RequestBody Film film) {
////        return filmService.updateFilm(film);
////    }
////
////    @GetMapping
////    public Collection<Film> getAll() {
////        return filmService.getAllFilms();
////    }
////
////    @GetMapping("/{id}")
////    public Optional<Film> getFilm(@PathVariable final long id) {
////        return filmService.getFilmById(id);
////    }
////
////    @PutMapping("/{id}/like/{userId}")
////    public void setLike(
////            @PathVariable final long id,
////            @PathVariable final long userId
////    ) {
////        userService.getUserById(userId); // если пользователя нет, NotFoundException
////        filmService.setLike(id, userId);
////    }
////
////    @DeleteMapping("/{id}/like/{userId}")
////    public void removeLike(
////            @PathVariable final long id,
////            @PathVariable final long userId
////    ) {
////        userService.getUserById(userId); // если пользователя нет, NotFoundException
////        filmService.removeLike(id, userId);
////    }
////
////    @GetMapping("/popular")
////    public Collection<Film> getPopularFilms(
////            @RequestParam(defaultValue = "10") final long count
////    ) {
////        return filmService.getPopularFilms(count);
////    }
////}