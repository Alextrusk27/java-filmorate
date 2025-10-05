package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.rowMapper.UserRowMapper;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper mapper;

    @Override
    public User save(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", user.getName());
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("birthday", user.getBirthday());
        jdbc.update("""
                INSERT INTO USERS (name, login, email, birthday)
                VALUES (:name, :login, :email, :birthday)
                """, params, keyHolder, new String[]{"id"});
        user.setId(keyHolder.getKeyAs(Long.class));
        return user;
    }

    @Override
    public User update(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("name", user.getName());
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("birthday", user.getBirthday());
        int rowsAffected = jdbc.update("""
                UPDATE USERS
                SET name = :name,
                    login = :login,
                    email = :email,
                    birthday = :birthday
                WHERE id = :id""", params);
        if (rowsAffected == 0) {
            throw new NotFoundException("Пользователь с ID: " + user.getId() + " не найден");
        }
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return jdbc.query("""
                SELECT * FROM USERS
                """, mapper);
    }

    @Override
    public Optional<User> get(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            return Optional.ofNullable(jdbc.queryForObject("""
                    SELECT * FROM USERS WHERE id = :id
                    """, params, mapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public void addFriend(long id, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("friend_id", friendId);
        int rowsAffected = jdbc.update("""
                UPDATE FRIENDS
                SET friend_confirmed = TRUE
                WHERE id = :friend_id AND friend_id = :id
                """, params);
        if (rowsAffected == 0) {
            jdbc.update("""
                    MERGE INTO FRIENDS (id, friend_id, friend_confirmed)
                    KEY(id, friend_id)
                    VALUES (:id, :friend_id, FALSE)
                    """, params);
        } else {
            jdbc.update("""
                    MERGE INTO FRIENDS (id, friend_id, friend_confirmed)
                    KEY(id, friend_id)
                    VALUES (:id, :friend_id, TRUE)
                    """, params);
        }
    }

    @Override
    public void removeFriend(long id, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("friend_id", friendId);
        jdbc.update("""
                UPDATE FRIENDS
                SET friend_confirmed = FALSE
                WHERE id = :friend_id AND friend_id = :id
                """, params);
        jdbc.update("""
                DELETE FROM FRIENDS
                WHERE id = :id AND friend_id = :friend_id
                """, params);
    }

    @Override
    public Collection<User> getFriends(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbc.query("""
                SELECT * FROM USERS
                WHERE id IN (SELECT friend_id
                             FROM FRIENDS
                             WHERE id = :id)
                """, params, mapper);
    }

    @Override
    public Collection<User> getCommonFriends(long id, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("friend_id", friendId);
        return jdbc.query("""
                SELECT *
                FROM USERS AS u
                WHERE u.id in (
                    SELECT f.friend_id
                    FROM FRIENDS AS f
                    WHERE f.id IN (:id, :friend_id)
                    GROUP BY f.friend_id
                    HAVING COUNT(DISTINCT f.id) = 2)
                """, params, mapper);
    }
}
