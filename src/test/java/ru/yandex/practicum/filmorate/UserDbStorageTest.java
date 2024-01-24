package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    UserDbStorage userStorage;
    User user1;
    User user2;
    User user3;

    @BeforeEach
    public void beforeEach() {
        userStorage = new UserDbStorage(jdbcTemplate);
        user1 = User.builder()
                .email("user21@yandex.ru")
                .login("user1")
                .name("Вася Пупкин")
                .birthday(LocalDate.of(2000, 5, 11))
                .build();
        user2 = User.builder()
                .email("user2@yandex.ru")
                .login("user2")
                .name("Александр Черданцев")
                .birthday(LocalDate.of(2000, 5, 11))
                .build();
        user3 = User.builder()
                .email("user3@yandex.ru")
                .login("user3")
                .name("Сонечка Людоедова")
                .birthday(LocalDate.of(2000, 5, 11))
                .build();
        userStorage.createNewUser(user1);
        userStorage.createNewUser(user2);
        userStorage.createNewUser(user3);
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    public void testCreateNewUserAndGetUser() {
        User user4 = User.builder()
                .email("user4@yandex.ru")
                .login("user4")
                .name("Борис Борисович Надеждин")
                .birthday(LocalDate.of(2000, 5, 11))
                .build();
        userStorage.createNewUser(user4);
        User user5 = userStorage.getUser(4);
        assertEquals(user4, user5);
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.findAllUsers();
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertTrue(users.contains(user3));
    }

    @Test
    public void testUpdateUser() {
        User user6 = User.builder()
                .email("user6@yandex.ru")
                .login("user6")
                .name("Анатолий Чубайс")
                .birthday(LocalDate.of(2000, 5, 11))
                .build();
        userStorage.createNewUser(user6);
        User user6Update = User.builder()
                .id(4)
                .email("user6Update@yandex.ru")
                .login("user6Update")
                .name("Анатолий Чубайс улучшенный")
                .birthday(LocalDate.of(2000, 5, 11))
                .build();
        userStorage.updateUser(user6Update);
        User user7 = userStorage.getUser(4);
        assertEquals(user6Update, user7);
    }

    @Test
    public void testDeleteUser() {
        userStorage.deleteUser(2);
        List<User> users = userStorage.findAllUsers();
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user3));
        assertFalse(users.contains(user2));
    }

    @Test
    public void testFindAllUsersId() {
        List<Integer> usersId = userStorage.findAllUsersId();
        assertTrue(usersId.contains(1));
        assertTrue(usersId.contains(2));
        assertTrue(usersId.contains(3));
    }

    @Test
    public void testFriends() {
        userStorage.addFriends(1, 2);
        userStorage.addFriends(1, 3);
        userStorage.addFriends(2, 3);
        userStorage.addFriends(3, 1);
        userStorage.addFriends(3, 2);
        List<User> user1Friends = userStorage.getFriends(1);
        List<User> user2Friends = userStorage.getFriends(2);
        List<User> user3Friends = userStorage.getFriends(3);
        assertTrue(user1Friends.contains(user2));
        assertTrue(user1Friends.contains(user3));
        assertTrue(user2Friends.contains(user3));
        assertFalse(user2Friends.contains(user1));
        assertTrue(user3Friends.contains(user2));
        assertTrue(user3Friends.contains(user1));
        userStorage.deleteFriends(3, 1);
        List<User> user3FriendsAfterDelete = userStorage.getFriends(3);
        assertTrue(user3FriendsAfterDelete.contains(user2));
        assertFalse(user3FriendsAfterDelete.contains(user1));
    }
}