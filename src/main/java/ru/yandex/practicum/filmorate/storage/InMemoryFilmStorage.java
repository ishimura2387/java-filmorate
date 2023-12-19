package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
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
    public int getCounterFilmId() {
        return counterFilmId;
    }

    @Override
    public List<Integer> findAllFilmsId() {
        return new ArrayList<>(films.keySet());
    }

}
