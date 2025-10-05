package ru.yandex.practicum.filmorate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.JdbcMpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@JdbcTest
@DisplayName("JdbcMpaRepository")
public class JdbcMpaRepositoryTest extends JdbcBaseRepositoryTest {
    @Autowired
    private JdbcMpaRepository mpaRepository;

    @Test
    @DisplayName("должен получить все mpa")
    void should_get_all_mpa() {
        Collection<Mpa> testMpa = new ArrayList<>();
        testMpa.add(getMpa(1));
        testMpa.add(getMpa(2));
        testMpa.add(getMpa(3));
        testMpa.add(getMpa(4));
        testMpa.add(getMpa(5));

        Collection<Mpa> allMpa = mpaRepository.getAll();

        Assertions.assertThat(testMpa)
                .usingRecursiveComparison()
                .isEqualTo(allMpa);
    }

    @Test
    @DisplayName("должен получить mpa по id")
    void should_get_mpa_by_id() {
        Mpa testMpa = getMpa(3);
        Optional<Mpa> optionalMpa = mpaRepository.get(3);

        Assertions.assertThat(optionalMpa)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(testMpa);
    }
}