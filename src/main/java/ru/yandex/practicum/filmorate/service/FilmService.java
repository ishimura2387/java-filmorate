package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    public List<Film> findAllFilms() {
        return inMemoryFilmStorage.findAllFilms();
    }

    public List<Integer> findAllFilmsId() {
        return inMemoryFilmStorage.findAllFilmsId();
    }

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.createNewFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public void deleteFilm(int id) {
        inMemoryFilmStorage.deleteFilm(id);
    }

    public Film getFilm(int id) {
        return inMemoryFilmStorage.getFilm(id);
    }


    public void addLike(int idUser, int idFilm) {
        getFilm(idFilm).getLikes().add(idUser);
    }

    public void deleteLike(int idUser, int idFilm) {
        getFilm(idFilm).getLikes().remove(Integer.valueOf(idUser));
    }

    public List<Film> getPopularFilms(int id) {
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

    public boolean findFilm(int id) {
        return findAllFilmsId().contains(id);
    }
}
