package ru.yandex.practicum.filmorate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@JdbcTest
@DisplayName("JdbcUserRepository")
public class JdbcUserRepositoryTest extends JdbcBaseRepositoryTest {
    @Autowired
    private JdbcUserRepository userRepository;

    @Test
    @DisplayName("должен находить пользователя по ID")
    public void should_return_user_when_find_by_id() {
        User testUser = getUser(FIRST_USER_ID);

        Optional<User> userOptional = userRepository.get(testUser.getId());

        Assertions.assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(testUser);
    }

    @Test
    @DisplayName("должен сохранять новых пользователей")
    public void should_save_user() {
        User user = getRandomUser();

        user.setId(userRepository.save(user).getId());

        Optional<User> userOptional = userRepository.get(user.getId());
        Assertions.assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @DisplayName("должен обновлять пользовтелей")
    public void should_update_user() {
        User secondUser = getRandomUser();
        secondUser.setId(FIRST_USER_ID);

        userRepository.update(secondUser);

        Optional<User> userOptional = userRepository.get(FIRST_USER_ID);
        Assertions.assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(secondUser);
    }

    @Test
    @DisplayName("должен находить всех пользователей")
    public void should_return_all_users_when_get_all() {
        Collection<User> users = List.of(getUser(FIRST_USER_ID), getUser(SECOND_USER_ID), getUser(THIRD_USER_ID),
                getUser(FORTH_USER_ID));

        Collection<User> allUsers = userRepository.getAll();

        Assertions.assertThat(users)
                .usingRecursiveComparison()
                .isEqualTo(allUsers);
    }

    @Test
    @DisplayName("должен находить друзей")
    public void should_return_friends() {
        Collection<User> testFriends = List.of(getUser(SECOND_USER_ID), getUser(THIRD_USER_ID), getUser(FORTH_USER_ID));

        Collection<User> friends = userRepository.getFriends(FIRST_USER_ID);

        Assertions.assertThat(testFriends)
                .usingRecursiveComparison()
                .isEqualTo(friends);
    }

    @Test
    @DisplayName("должен находить общих друзей")
    public void should_return_common_friends() {
        Collection<User> testCommonFriends = List.of(getUser(THIRD_USER_ID), getUser(FORTH_USER_ID));

        Collection<User> commonFriends = userRepository.getCommonFriends(FIRST_USER_ID, SECOND_USER_ID);

        Assertions.assertThat(testCommonFriends)
                .usingRecursiveComparison()
                .isEqualTo(commonFriends);
    }

    @Test
    @DisplayName("должен добавлять в друзья")
    public void should_add_to_friends() {
        userRepository.addFriend(THIRD_USER_ID, FORTH_USER_ID);

        Collection<User> testFriends = List.of(getUser(FORTH_USER_ID));
        Collection<User> friends = userRepository.getFriends(THIRD_USER_ID);

        Assertions.assertThat(testFriends)
                .usingRecursiveComparison()
                .isEqualTo(friends);
    }

    @Test
    @DisplayName("должен удалять из друзей")
    public void should_delete_from_friends() {
        userRepository.removeFriend(FIRST_USER_ID, SECOND_USER_ID);

        Collection<User> testFriends = List.of(getUser(THIRD_USER_ID), getUser(FORTH_USER_ID));
        Collection<User> friends = userRepository.getFriends(FIRST_USER_ID);

        Assertions.assertThat(testFriends)
                .usingRecursiveComparison()
                .isEqualTo(friends);
    }
}