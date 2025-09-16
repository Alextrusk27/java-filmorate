package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class Film {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Long duration;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Set<Long> likes;
}