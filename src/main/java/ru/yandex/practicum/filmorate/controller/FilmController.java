package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @GetMapping()
    public List<Film> findAllFilms() {
        List<Film> films = filmService.findAllFilms();
        log.debug("Обработка запроса GET /films; Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Создан фильм: {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (filmService.findFilm(film.getId())) {
            filmService.updateFilm(film);
            log.debug("Изменен фильм: {}", film);
            return film;
        } else {
            log.debug("Фильм не найден!");
            throw new NullObjectException("Фильм не найден!");
        }
    }

    @DeleteMapping
    public void deleteFilm(@Valid @RequestBody Film film) {
        if (filmService.findFilm(film.getId())) {
            filmService.deleteFilm(film.getId());
            log.debug("Удален фильм: {}", film);
        } else {
            log.debug("Фильм не найден!");
            throw new NullObjectException("Фильм не найден!");
        }
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        if (filmService.findFilm(id)) {
            Film film = filmService.getFilm(id);
            log.debug("Запрошен фильм: {}", film);
            return film;
        } else {
            log.debug("Фильм не найден!");
            throw new NullObjectException("Фильм не найден!");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int userId, @PathVariable int id) {
        boolean checkFilm = filmService.findFilm(id);
        if (checkFilm && userService.findUser(id) && !getFilm(id).getLikes().contains(userId)) {
            filmService.addLike(userId, id);
            log.debug("Запрос добавления лайка");
        } else {
            if (!checkFilm) {
                log.debug("Фильм не найден. ID: {}", id);
                throw new NullObjectException("Фильм не найден!");
            } else {
                log.debug("Пользователь не найден. ID: {}", userId);
                throw new NullObjectException("Пользователь не найден!");
            }
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int userId, @PathVariable int id) {
        boolean checkFilm = filmService.findFilm(id);
        if (checkFilm && userService.findUser(userId)) {
            filmService.deleteLike(userId, id);
            log.debug("Запрос удаления лайка");
        } else {
            if (!checkFilm) {
                log.debug("Фильм не найден. ID: {}", id);
                throw new NullObjectException("Фильм не найден!");
            } else {
                log.debug("Пользователь не найден. ID: {}", userId);
                throw new NullObjectException("Пользователь не найден!");
            }
        }
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("Запрос популярных фильмов");
        return filmService.getPopularFilms(count);
    }

}

