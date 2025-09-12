package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Set<Long> friends;
}