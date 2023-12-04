package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationUserExeption;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int counterUserId = 1;

    @GetMapping
    public List<User> findAllUsers() {
        log.debug("Обработка запроса GET /users; Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationUserExeption {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(counterUserId);
        users.put(counterUserId, user);
        counterUserId++;
        return user;
    }

    @PutMapping
    public User patchUser(@Valid @RequestBody User user) throws ValidationUserExeption {
        if (users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.replace(user.getId(), user);
            return user;
        } else {
            log.debug("Пользователь не найден!");
            throw new ValidationUserExeption("Пользователь не найден!");
        }
    }
}
