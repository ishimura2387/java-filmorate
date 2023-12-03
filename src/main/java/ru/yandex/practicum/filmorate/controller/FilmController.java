package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationFilmExeption;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int counterFilmId = 1;
    private final LocalDate startCinemaInWorldDate = LocalDate.of(1895, 12, 28);

    @GetMapping()
    public ArrayList<Film> findAllFilms() {
        log.debug("Обработка запроса GET /films; Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationFilmExeption {
        if (isValid(film)) {
            film.setId(counterFilmId);
            films.put(counterFilmId, film);
            counterFilmId++;
            log.debug("Изменен фильм: {}", film);
            return film;
        } else {
            log.debug("Фильм не прошел валидацию.");
            return null;
        }
    }


    @PutMapping()
    public Film patchFilm(@Valid @RequestBody Film film) throws ValidationFilmExeption {
        if (isValid(film) && films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.debug("Изменен фильм: {}", film);
            return film;
        } else {
            log.debug("Фильм не прошел валидацию.");
            throw new ValidationFilmExeption("Фильм не прошел валидацию.");
        }
    }

    public boolean isValid(Film film) throws ValidationFilmExeption {
        if (film.getDescription().length() > 200) {
            log.debug("ValidationFilmExeption: максимальная длина описания — 200 символов.");
            throw new ValidationFilmExeption("Максимальная длина описания — 200 символов.");
        } else if (film.getReleaseDate().isBefore(startCinemaInWorldDate)) {
            log.debug("ValidationFilmExeption: фильм не может выйти раньше изобретения Синематографа.");
            throw new ValidationFilmExeption("Фильм не может выйти раньше изобретения Синематографа.");
        } else if (film.getDuration() <= 0) {
            log.debug("ValidationFilmExeption: продолжительность фильма должна быть больше 0");
            throw new ValidationFilmExeption("Продолжительность фильма должна быть больше 0");
        } else {
            return true;
        }
    }
}
