package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exeption.InvalidDescriptionFilmExeption;
import ru.yandex.practicum.filmorate.exeption.InvalidNameFilmExeption;
import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FilmController {

    private final List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Обработка запроса GET /films; Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping(value = "/films")
    public void create(@RequestBody Film film) throws InvalidNameFilmExeption, InvalidDescriptionFilmExeption {
        log.debug("Добавлен фильм: {}", film);
        if (film.getName().equals("") || film.getName() == null) {
            log.debug("InvalidNameFilmExeption: название фильма не может быть пустым.");
            throw new InvalidNameFilmExeption("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            log.debug("InvalidDescriptionFilmExeption: максимальная длина описания — 200 символов.");
            throw new InvalidDescriptionFilmExeption("Максимальная длина описания — 200 символов.");
        }
        films.add(film);
    }

}
