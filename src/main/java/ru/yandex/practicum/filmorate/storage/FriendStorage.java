package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    List<User> getFriends(int id);

    void addFriends(int idUser, int idFriend);

    void deleteFriends(int idUser, int idFriend);

    List<User> findTotalFriends(int idUser1, int idUser2);
}
