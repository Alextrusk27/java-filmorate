package ru.yandex.practicum.filmorate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@JdbcTest
@DisplayName("JdbcGenreRepository")
public class JdbcGenreRepositoryTest extends JdbcBaseRepositoryTest {
    @Autowired
    private JdbcGenreRepository genreRepository;

    @Test
    @DisplayName("должен получить все жанры")
    void should_get_all_genres() {
        Collection<Genre> testGenres = new ArrayList<>();
        testGenres.add(getGenre(1));
        testGenres.add(getGenre(2));
        testGenres.add(getGenre(3));
        testGenres.add(getGenre(4));
        testGenres.add(getGenre(5));
        testGenres.add(getGenre(6));

        Collection<Genre> allGenres = genreRepository.getAll();

        Assertions.assertThat(testGenres)
                .usingRecursiveComparison()
                .isEqualTo(allGenres);
    }

    @Test
    @DisplayName("должен получить жанр по id")
    void should_get_genre_by_id() {
        Genre testGenre = getGenre(1);
        Optional<Genre> optionalGenre = genreRepository.get(1);

        Assertions.assertThat(optionalGenre)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(testGenre);
    }
}