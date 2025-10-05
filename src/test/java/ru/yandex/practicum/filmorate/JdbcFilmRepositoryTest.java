package ru.yandex.practicum.filmorate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;

import java.util.Collection;
import java.util.LinkedHashSet;

import static ru.yandex.practicum.filmorate.service.FilmService.GET_ALL_FILMS;

@JdbcTest
@DisplayName("JdbcFilmRepository")
public class JdbcFilmRepositoryTest extends JdbcBaseRepositoryTest {
    @Autowired
    private JdbcFilmRepository filmRepository;

    @Test
    @DisplayName("должен находить фильм по ID")
    void should_return_film_when_find_by_id() {
        Film testFilm = getFilm(FIRST_FILM_ID);

        Film film = filmRepository.get(FIRST_FILM_ID);

        Assertions.assertThat(testFilm)
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("должен находить все фильмы")
    void should_return_all_when_get_all() {
        Collection<Film> allTestFilms = new LinkedHashSet<>();
        allTestFilms.add(getFilm(FIRST_FILM_ID));
        allTestFilms.add(getFilm(SECOND_FILM_ID));
        allTestFilms.add(getFilm(THIRD_FILM_ID));

        Collection<Film> allFilms = filmRepository.getFilms(GET_ALL_FILMS);

        Assertions.assertThat(allTestFilms)
                .usingRecursiveComparison()
                .isEqualTo(allFilms);
    }

    @Test
    @DisplayName("должен сохранить новые фильм")
    void should_save_new_film() {
        Film testFilm = getFilm(NEW_FILM_ID);

        filmRepository.save(testFilm);

        Film film = filmRepository.get(NEW_FILM_ID);
        Assertions.assertThat(testFilm)
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("должен обновить фильм")
    void should_update_new_film() {
        Film testFilm = getFilm(NEW_FILM_ID);
        testFilm.setId(FIRST_FILM_ID);

        filmRepository.update(testFilm);

        Film film = filmRepository.get(FIRST_FILM_ID);
        Assertions.assertThat(testFilm)
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("должен получить список популярных фильмов по убыванию")
    void should_get_popular_films_by_decreasing() {
        LinkedHashSet<Film> testFilms = new LinkedHashSet<>();
        testFilms.add(getFilm(SECOND_FILM_ID));
        testFilms.add(getFilm(FIRST_FILM_ID));
        testFilms.add(getFilm(THIRD_FILM_ID));

        Collection<Film> topFilms = filmRepository.getFilms(5);

        Assertions.assertThat(testFilms)
                .usingRecursiveComparison()
                .isEqualTo(topFilms);
    }

    @Test
    @DisplayName("должен поставить лайк фильму")
    void should_set_like() {
        filmRepository.setLike(THIRD_FILM_ID, FIRST_USER_ID);
        filmRepository.setLike(THIRD_FILM_ID, SECOND_USER_ID);
        filmRepository.setLike(THIRD_FILM_ID, THIRD_USER_ID);
        filmRepository.setLike(THIRD_FILM_ID, FORTH_USER_ID);

        LinkedHashSet<Film> testFilms = new LinkedHashSet<>();
        testFilms.add(getFilm(THIRD_FILM_ID));
        testFilms.add(getFilm(SECOND_FILM_ID));
        testFilms.add(getFilm(FIRST_FILM_ID));
        Collection<Film> topFilms = filmRepository.getFilms(5);

        Assertions.assertThat(testFilms)
                .usingRecursiveComparison()
                .isEqualTo(topFilms);
    }

    @Test
    @DisplayName("должен удалить лайк у фильма")
    void should_remove_like() {
        filmRepository.removeLike(SECOND_FILM_ID, FIRST_USER_ID);
        filmRepository.removeLike(SECOND_FILM_ID, SECOND_USER_ID);

        LinkedHashSet<Film> testFilms = new LinkedHashSet<>();
        testFilms.add(getFilm(FIRST_FILM_ID));
        testFilms.add(getFilm(SECOND_FILM_ID));
        testFilms.add(getFilm(THIRD_FILM_ID));
        Collection<Film> topFilms = filmRepository.getFilms(5);

        Assertions.assertThat(testFilms)
                .usingRecursiveComparison()
                .isEqualTo(topFilms);
    }
}