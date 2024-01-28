package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> getPopularFilms(int count);

    boolean checkLikes(int idUser, int idFilm);
}
