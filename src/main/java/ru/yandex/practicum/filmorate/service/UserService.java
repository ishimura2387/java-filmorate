package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@Component
public class UserService {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage inMemoryUserStorage;

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
        inMemoryUserStorage.addFriends(idUser1, idUser2);
    }

    public void deleteFriends(int idUser1, int idUser2) {
        inMemoryUserStorage.deleteFriends(idUser1, idUser2);
    }

    public List<User> findTotalFriends(int idUser1, int idUser2) {
        List<User> totalFriends = new ArrayList<>();
        List<User> friendsUser1 = inMemoryUserStorage.getFriends(idUser1);
        List<User> friendsUser2 = inMemoryUserStorage.getFriends(idUser2);
        for (User user : friendsUser2) {
            if (friendsUser1.contains(user)) {
                totalFriends.add(user);
            }
        }
        return totalFriends;
    }

    public List<User> getFriends(int id) {
        return inMemoryUserStorage.getFriends(id);
    }

    public boolean findUser(int id) {
        return findAllUsersId().contains(id);
    }
}
