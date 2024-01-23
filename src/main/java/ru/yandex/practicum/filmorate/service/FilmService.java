package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Component
public class FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage inMemoryFilmStorage;

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
        inMemoryFilmStorage.addLike(idUser, idFilm);
    }

    public void deleteLike(int idUser, int idFilm) {
        inMemoryFilmStorage.deleteLike(idUser, idFilm);
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

    public List<Genre> findAllGenres() {
        return inMemoryFilmStorage.findAllGenres();
    }

    public Genre getGenre(int id) {
        return inMemoryFilmStorage.getGenre(id);
    }

    public List<Mpa> findAllMpas() {
        return inMemoryFilmStorage.findAllMpas();
    }

    public Mpa getMpa(int id) {
        return inMemoryFilmStorage.getMpa(id);
    }

    public List<Integer> findAllMpasId() {
        return inMemoryFilmStorage.findAllMpasId();
    }

    public List<Integer> findAllGenresId() {
        return inMemoryFilmStorage.findAllGenresId();
    }
}
