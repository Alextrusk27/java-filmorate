package ru.yandex.practicum.filmorate.dto.films;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

public class NewFilmRequest {
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может быть длиннее 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть больше нуля")
    private Long duration;

    private Mpa mpa;

    private Set<Genre> genres;
}
