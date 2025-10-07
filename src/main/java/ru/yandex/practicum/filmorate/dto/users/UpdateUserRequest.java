package ru.yandex.practicum.filmorate.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    @NonNull
    Long id;

    @Size(min = 3, max = 50, message = "Логин должен содержать от 3 до 50 символов")
    private String login;

    @Size(max = 100, message = "Имя не может быть длиннее 100 символов")
    private String name;

    @Email(message = "Неверный формат email")
    private String email;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public boolean hasLogin() {
        return login != null && !login.isEmpty();
    }

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasEmail() {
        return email != null && !email.isEmpty();
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}

