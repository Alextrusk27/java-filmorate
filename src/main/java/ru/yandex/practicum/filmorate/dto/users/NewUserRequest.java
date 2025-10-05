package ru.yandex.practicum.filmorate.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 50, message = "Логин должен содержать от 3 до 50 символов")
    private String login;

    @Size(max = 100, message = "Имя не может быть длиннее 100 символов")
    private String name;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }
}
