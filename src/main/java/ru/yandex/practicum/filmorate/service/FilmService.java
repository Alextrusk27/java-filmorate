//package ru.yandex.practicum.filmorate.service;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.repository.UserRepository;
//
//import java.util.Collection;
//import java.util.Comparator;
//import java.util.Optional;
//
//@Slf4j
//@Service
//@AllArgsConstructor
//public class FilmService {
//
//    private final UserRepository<Film> filmStorage;
//
//    public Film createFilm(Film film) {
//        Film result = filmStorage.save(film);
//        log.info("Создан фильм {} ID {}", film.getName(), film.getId());
//        return result;
//    }
//
//    public Film updateFilm(Film film) {
//        Film result = filmStorage.update(film);
//        log.info("Изменение фильма {} ID {} завершено", result.getName(), result.getId());
//        return result;
//    }
//
//    public Collection<Film> getAllFilms() {
//        log.info("Получение списка фильмов");
//        return filmStorage.getAll();
//    }
//
//    public Optional<Film> getFilmById(long id) {
//        log.info("Запрос на поиск фильма с ID {}", id);
//        return filmStorage.get(id);
//    }
//
//    public void setLike(long id, long userId) {
//        Optional<Film> film = getFilmById(id);
//        film.get().getLikes().add(userId);
//        log.info("Поставлен лайк фильму с ID {} пользователем с ID {}", id, userId);
//    }
//
//    public void removeLike(long id, long userId) {
//        Optional<Film> film = getFilmById(id);
//        film.get().getLikes().remove(userId);
//        log.info("Удален лайк с фильма с ID {} пользователем с ID {}", id, userId);
//    }
//
//    public Collection<Film> getPopularFilms(long count) {
//        log.info("Запрос на топ {} попурных фильмов", count);
//        return filmStorage.getAll().stream()
//                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
//                .limit(count)
//                .toList();
//    }
//}