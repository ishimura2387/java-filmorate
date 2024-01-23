package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAllUsers() {
        List<User> users = userService.findAllUsers();
        log.debug("Обработка запроса GET /users; Текущее количество пользователей: {}",
                users.size());
        return users;
    }


    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Создан пользователь: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (userService.findUser(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userService.updateUser(user);
            log.debug("Пользователь изменен: {}", user);
            return user;
        } else {
            log.debug("Пользователь не найден!");
            throw new NullObjectException("Пользователь не найден!");
        }
    }

    @DeleteMapping
    public void deleteUser(@Valid @RequestBody User user) {
        if (userService.findUser(user.getId())) {
            userService.deleteUser(user);
            log.debug("Пользователь удален: {}", user);
        } else {
            log.debug("Пользователь не найден!");
            throw new NullObjectException("Пользователь не найден!");
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        if (userService.findUser(id)) {
            User user = userService.getUser(id);
            log.debug("Запрошен пользователь: {}", user);
            return user;
        } else {
            log.debug("Пользователь не найден!");
            throw new NullObjectException("Пользователь не найден!");
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable int id, @PathVariable int friendId) {
        if (userService.findUser(id) && userService.findUser(friendId) && !getFriends(id).contains(friendId) &&
                !getFriends(friendId).contains(id)) {
            userService.addFriends(id, friendId);
            log.debug("Добавление в друзья прошло успешно");
        } else {
            log.debug("Ошибка добавления в друзья");
            throw new NullObjectException("Пользователь не найден!");
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        if (userService.findUser(id) &&
                userService.findUser(friendId)) {
            userService.deleteFriends(id, friendId);
            log.debug("Удаление из друзей прошло успешно");
        } else {
            log.debug("Ошибка удаления из друзей");
            throw new NullObjectException("Пользователь не найден!");
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        if (userService.findUser(id)) {
            log.debug("Обработка запроса getFriends успешно");
            return userService.getFriends(id);
        } else {
            log.debug("Пользователь не найден!");
            throw new NullObjectException("Пользователь не найден!");
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getTotalFriends(@PathVariable int id, @PathVariable int otherId) {
        if (userService.findUser(id) && userService.findUser(otherId)) {
            log.debug("Поиск общих друзей прошел успешно");
            return userService.findTotalFriends(id, otherId);
        } else {
            log.debug("Ошибка поиска общих друзей");
            throw new NullObjectException("Пользователь не найден!");
        }
    }
}
