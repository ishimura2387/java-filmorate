package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationUserExeption;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int counterUserId = 1;

    @GetMapping
    public ArrayList<User> findAllUsers() {
        log.debug("Обработка запроса GET /users; Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
   }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationUserExeption {
        if (isValid(user)) {
            if (user.getName().equals("") || user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(counterUserId);
            users.put(counterUserId, user);
            counterUserId++;
            return user;
        } else {
            log.debug("Пользователь не прошел валидацию.");
            return null;
        }
    }

    @PutMapping
    public User patchUser(@Valid @RequestBody User user) throws ValidationUserExeption {
        if (isValid(user) && users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            return user;
        } else {
            log.debug("Пользователь не прошел валидацию.");
            throw new ValidationUserExeption("Пользователь не прошел валидацию.");
        }
    }

    public boolean isValid(User user) throws ValidationUserExeption {
        if (user.getLogin().contains(" ")) {
            log.debug("ValidationUserExeption: логин не может содержать пробелы.");
            throw new ValidationUserExeption("Логин не может содержать пробелы.");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return true;
        } else {
            return true;
        }

    }
}
