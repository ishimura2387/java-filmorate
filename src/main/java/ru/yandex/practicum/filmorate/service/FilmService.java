package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmDbStorage;
    private final UserService userService;

    public List<Film> findAllFilms() {
        List<Film> films = filmDbStorage.findAllFilms();
        log.debug("Обработка запроса GET /films; Текущее количество фильмов: {}", films.size());
        return films;
    }

    public Film createFilm(Film film) {
        log.debug("Создан фильм: {}", film);
        return filmDbStorage.createNewFilm(film);
    }

    public Film updateFilm(Film film) {
        checkFilm(film.getId());
        log.debug("Изменен фильм: {}", film);
        return filmDbStorage.updateFilm(film);
    }

    public void deleteFilm(int id) {
        checkFilm(id);
        log.debug("Удален фильм c id: {}", id);
        filmDbStorage.deleteFilm(id);
    }

    public Film getFilm(int id) {
        checkFilm(id);
        log.debug("Запрошен фильм c id: {}", id);
        return filmDbStorage.getFilm(id);
    }


    public void addLike(int idUser, int idFilm) {
        checkFilm(idFilm);
        userService.checkUser(idUser);
        log.debug("Запрос добавления лайка");
        filmDbStorage.addLike(idUser, idFilm);
    }

    public void deleteLike(int idUser, int idFilm) {
        checkFilm(idFilm);
        userService.checkUser(idUser);
        log.debug("Запрос удаления лайка");
        filmDbStorage.deleteLike(idUser, idFilm);
    }

    public List<Film> getPopularFilms(int id) {
        log.debug("Запрос популярных фильмов");
        List<Film> films = findAllFilms();
        if (films.size() < id) {
            id = films.size();
        }
        Collections.sort(films);
        List<Film> popularFilms = new ArrayList<>();
        for (int i = 0; i < id; i++) {
            popularFilms.add(films.get(i));
        }
        return popularFilms;
    }

    public void checkFilm(int id) {
        if (!filmDbStorage.findAllFilmsId().contains(id)) {
            log.debug("Фильм не найден!");
            throw new NullObjectException("Фильм не найден!");
        }
    }
}
