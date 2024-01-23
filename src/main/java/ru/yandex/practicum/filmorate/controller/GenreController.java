package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/genres")
public class GenreController {

    private final FilmService genreService;

    @GetMapping
    public List<Genre> findAllGenre() {
        List<Genre> genres = genreService.findAllGenres();
        log.debug("Обработка запроса GET /genres; Текущее количество жанров: {}",
                genres.size());
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        if (genreService.findAllGenresId().contains(id)) {
            Genre genre = genreService.getGenre(id);
            log.debug("Запрошен жанр: {}", genre);
            return genre;
        } else {
            log.debug("жанр не найден!");
            throw new NullObjectException("жанр не найден!");
        }
    }
}
