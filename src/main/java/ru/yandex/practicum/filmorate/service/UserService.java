package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userDbStorage;

    public List<User> findAllUsers() {
        List<User> users = userDbStorage.findAllUsers();
        log.debug("Обработка запроса GET /users; Текущее количество пользователей: {}",
                users.size());
        return users;
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Создан пользователь: {}", user);
        return userDbStorage.createNewUser(user);
    }

    public User updateUser(@RequestBody User user) {
        checkUser(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Пользователь изменен: {}", user);
        return userDbStorage.updateUser(user);
    }

    public void deleteUser(@RequestBody User user) {
        checkUser(user.getId());
        log.debug("Пользователь удален: {}", user);
        userDbStorage.deleteUser(user.getId());
    }

    public User getUser(int id) {
        checkUser(id);
        log.debug("Запрошен  c id: {}", id);
        return userDbStorage.getUser(id);
    }


    public void addFriends(int idUser1, int idUser2) {
        checkUser(idUser1);
        checkUser(idUser2);
        log.debug("Обработка запроса добавления в друзья");
        userDbStorage.addFriends(idUser1, idUser2);
    }

    public void deleteFriends(int idUser1, int idUser2) {
        checkUser(idUser1);
        checkUser(idUser2);
        log.debug("Обработка запроса удаления из друзей");
        userDbStorage.deleteFriends(idUser1, idUser2);
    }

    public List<User> findTotalFriends(int idUser1, int idUser2) {
        checkUser(idUser1);
        checkUser(idUser2);
        log.debug("Обработка запроса поиска общих друзей");
        List<User> totalFriends = new ArrayList<>();
        List<User> friendsUser1 = userDbStorage.getFriends(idUser1);
        List<User> friendsUser2 = userDbStorage.getFriends(idUser2);
        for (User user : friendsUser2) {
            if (friendsUser1.contains(user)) {
                totalFriends.add(user);
            }
        }
        return totalFriends;
    }

    public List<User> getFriends(int id) {
        checkUser(id);
        log.debug("Обработка запроса getFriends");
        return userDbStorage.getFriends(id);
    }

    public void checkUser(int id) {
        if (!userDbStorage.findAllUsersId().contains(id)) {
            log.debug("Пользователь не найден!");
            throw new NullObjectException("Пользователь не найден!");
        }
    }
}
