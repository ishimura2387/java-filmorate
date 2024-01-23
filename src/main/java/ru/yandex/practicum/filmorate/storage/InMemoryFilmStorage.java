package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int counterFilmId = 1;

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createNewFilm(Film film) {
        film.setId(counterFilmId);
        films.put(counterFilmId, film);
        counterFilmId++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return films.replace(film.getId(), film);
    }

    @Override
    public Film getFilm(int id) {
        return films.get(id);
    }

    @Override
    public void deleteFilm(int id) {
        films.remove(id);
    }

    @Override
    public List<Integer> findAllFilmsId() {
        return new ArrayList<>(films.keySet());
    }

    @Override
    public List<Genre> findAllGenres() {
        return null;
    }

    @Override
    public Genre getGenre(int id) {
        return null;
    }


    @Override
    public List<Mpa> findAllMpas() {
        return null;
    }

    @Override
    public Mpa getMpa(int id) {
        return null;
    }

    @Override
    public void addLike(int userId, int filmId) {
    }

    @Override
    public void deleteLike(int userId, int filmId) {
    }

    @Override
    public List<Integer> findAllMpasId() {
        return null;
    }

    @Override
    public List<Integer> findAllGenresId() {
        return null;
    }
}
