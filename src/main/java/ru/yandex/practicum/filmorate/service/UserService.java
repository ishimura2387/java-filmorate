package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import ru.yandex.practicum.filmorate.storage.UserStorage;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage inMemoryUserStorage;

    public List<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }

    public List<Integer> findAllUsersId() {
        return inMemoryUserStorage.findAllUsersId();
    }

    public User createUser(User user) {
        return inMemoryUserStorage.createNewUser(user);
    }

    public User updateUser(@RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public User deleteUser(@RequestBody User user) {
        inMemoryUserStorage.deleteUser(user.getId());
        return user;
    }

    public User getUser(int id) {
        return inMemoryUserStorage.getUser(id);
    }


    public void addFriends(int idUser1, int idUser2) {
        getUser(idUser1).getFriends().add(getUser(idUser2));
        getUser(idUser2).getFriends().add(getUser(idUser1));
    }

    public void deleteFriends(int idUser1, int idUser2) {
        getUser(idUser1).getFriends().remove(getUser(idUser2));
        getUser(idUser2).getFriends().remove(getUser(idUser1));
    }

    public List<User> findTotalFriends(int idUser1, int idUser2) {
        List<User> totalFriends = new ArrayList<>();
        User user1 = getUser(idUser1);
        List<User> friendsUser1 = user1.getFriends();
        User user2 = getUser(idUser2);
        List<User> friendsUser2 = user2.getFriends();
        for (User user : friendsUser2) {
            if (friendsUser1.contains(user)) {
                totalFriends.add(user);
            }
        }
        return totalFriends;
    }

    public List<User> getF(int id) {
        return getUser(id).getFriends();
    }
}
