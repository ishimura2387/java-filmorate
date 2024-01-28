package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class LikesService {
    @Autowired
    @Qualifier("likesDbStorage")
    private final LikesStorage likesDbStorage;
    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmDbStorage;
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userDbStorage;

    public void addLike(int idUser, int idFilm) {
        checkFilm(idFilm);
        checkUser(idUser);
        log.debug("Запрос добавления лайка");
        likesDbStorage.addLike(idUser, idFilm);
    }

    public void deleteLike(int idUser, int idFilm) {
        checkLikes(idUser, idFilm);
        log.debug("Запрос удаления лайка");
        likesDbStorage.deleteLike(idUser, idFilm);
    }

    public List<Film> getPopularFilms(int id) {
        return likesDbStorage.getPopularFilms(id);
    }

    private void checkLikes(int idUser, int idFilm) {
        if (!likesDbStorage.checkLikes(idUser, idFilm)) {
            log.debug("Лайк отсутствует!");
            throw new NullObjectException("Лайк отсутствует!");
        }
    }
    private void checkFilm(int id) {
        if (!filmDbStorage.findAllFilmsId().contains(id)) {
            log.debug("Фильм не найден!");
            throw new NullObjectException("Фильм не найден!");
        }
    }
    private void checkUser(int id) {
        if (!userDbStorage.findAllUsersId().contains(id)) {
            log.debug("Пользователь не найден!");
            throw new NullObjectException("Пользователь не найден!");
        }
    }
}
