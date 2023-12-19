package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int counterUserId = 1;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createNewUser(User user) {
        user.setId(counterUserId);
        users.put(counterUserId, user);
        counterUserId++;
        return user;
    }

    @Override
    public User updateUser(User user) {
       return users.replace(user.getId(), user);
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public void deleteUser(int id) {
      users.remove(id);
    }
    @Override
    public List<Integer> findAllUsersId() {
        return new ArrayList<>(users.keySet());
    }
}
