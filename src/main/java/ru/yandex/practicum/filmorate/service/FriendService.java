package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendService {
    @Autowired
    @Qualifier("friendDbStorage")
    private final FriendStorage friendDbStorage;
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userDbStorage;

    public void addFriends(int idUser1, int idUser2) {
        checkUser(idUser1);
        checkUser(idUser2);
        log.debug("Обработка запроса добавления в друзья");
        friendDbStorage.addFriends(idUser1, idUser2);
    }

    public void deleteFriends(int idUser1, int idUser2) {
        checkUser(idUser1);
        checkUser(idUser2);
        log.debug("Обработка запроса удаления из друзей");
        friendDbStorage.deleteFriends(idUser1, idUser2);
    }

    public List<User> findTotalFriends(int idUser1, int idUser2) {
        checkUser(idUser1);
        checkUser(idUser2);
        log.debug("Обработка запроса getFriends");
        return friendDbStorage.findTotalFriends(idUser1, idUser2);
    }

    public List<User> getFriends(int id) {
        checkUser(id);
        log.debug("Обработка запроса getFriends");
        return friendDbStorage.getFriends(id);
    }
    
    private void checkUser(int id) {
        if (!userDbStorage.findAllUsersId().contains(id)) {
            log.debug("Пользователь не найден!");
            throw new NullObjectException("Пользователь не найден!");
        }
    }
}
