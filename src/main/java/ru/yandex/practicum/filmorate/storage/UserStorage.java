package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAllUsers();

    User createNewUser(User user);

    User updateUser(User user);

    User getUser(int id);

    void deleteUser(int id);

    List<Integer> findAllUsersId();
}
