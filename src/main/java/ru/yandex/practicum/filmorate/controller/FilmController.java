package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationFilmExeption;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int counterFilmId = 1;

    @GetMapping()
    public List<Film> findAllFilms() {
        log.debug("Обработка запроса GET /films; Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(counterFilmId);
        films.put(counterFilmId, film);
        counterFilmId++;
        log.debug("Изменен фильм: {}", film);
        return film;
    }


    @PutMapping()
    public Film patchFilm(@Valid @RequestBody Film film) throws ValidationFilmExeption {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.debug("Изменен фильм: {}", film);
            return film;
        } else {
            log.debug("Фильм не найден!");
            throw new ValidationFilmExeption("Фильм не найден!");
        }
    }
}
