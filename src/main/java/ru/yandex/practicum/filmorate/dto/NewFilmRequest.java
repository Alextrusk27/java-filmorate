package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
public class NewFilmRequest {
    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 30, message = "Название не может быть длиннее 30 символов")
    private String name;

    @Size(max = 200, message = "Описание не может быть длиннее 200 символов")
    private String description;

    @Past(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть больше нуля")
    private Long duration;

    private Mpa mpa;
    private Set<Genre> genres;
}