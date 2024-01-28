package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAllFilms();

    Film createNewFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(int id);

    void deleteFilm(int id);

    List<Integer> findAllFilmsId();
}
