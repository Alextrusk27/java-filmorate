package ru.yandex.practicum.filmorate.repository.rowMapper;

import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        Timestamp birthdayDate = rs.getTimestamp("birthday");
        user.setBirthday(birthdayDate.toLocalDateTime().toLocalDate());
        return user;
    }
}
