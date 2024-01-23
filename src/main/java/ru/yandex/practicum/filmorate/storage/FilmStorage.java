package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {

    List<Film> findAllFilms();

    Film createNewFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(int id);

    void deleteFilm(int id);

    List<Integer> findAllFilmsId();

    List<Genre> findAllGenres();

    Genre getGenre(int id);

    List<Mpa> findAllMpas();

    Mpa getMpa(int id);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Integer> findAllMpasId();

    List<Integer> findAllGenresId();

}
