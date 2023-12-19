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
        log.debug("Обработка запроса GET /films; Текущее количество фильмов: {}", filmService.findAllFilms().size());
        return filmService.findAllFilms();
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Создан фильм: {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (filmService.findAllFilmsId().contains(film.getId())) {
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
        if (filmService.findAllFilms().contains(film)) {
            filmService.deleteFilm(film.getId());
            log.debug("Удален фильм: {}", filmService.getFilm(film.getId()));
        } else {
            log.debug("Фильм не найден!");
            throw new NullObjectException("Фильм не найден!");
        }
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        if (filmService.findAllFilms().contains(filmService.getFilm(id))) {
            log.debug("Запрошен фильм: {}", filmService.getFilm(id));
            return filmService.getFilm(id);
        } else {
            log.debug("Фильм не найден!");
            throw new NullObjectException("Фильм не найден!");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int userId, @PathVariable int id) {
        if (findAllFilms().contains(getFilm(id)) &&
                userService.findAllUsers().contains(userService.getUser(userId)) &&
                !getFilm(id).getLikes().contains(userService.getUser(userId))) {
            filmService.addLike(userId, id);
            log.debug("Запрос добавления лайка");
        } else {
            if (!filmService.findAllFilms().contains(filmService.getFilm(id))) {
                log.debug("Фильм не найден: {}", filmService.getFilm(id));
                throw new NullObjectException("Фильм не найден!");
            } else {
                log.debug("Пользователь не найден: {}", userService.getUser(userId));
                throw new NullObjectException("Пользователь не найден!");
            }
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int userId, @PathVariable int id) {
        if (findAllFilms().contains(filmService.getFilm(id)) &&
                userService.findAllUsers().contains(userService.getUser(userId))) {
            filmService.deleteLike(userId, id);
            log.debug("Запрос удаления лайка");
        } else {
            if (!findAllFilms().contains(filmService.getFilm(id))) {
                log.debug("Фильм не найден: {}", filmService.getFilm(id));
                throw new NullObjectException("Фильм не найден!");
            } else {
                log.debug("Пользователь не найден: {}", userService.getUser(userId));
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

