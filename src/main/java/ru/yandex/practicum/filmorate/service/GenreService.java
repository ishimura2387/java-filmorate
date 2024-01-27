package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    @Autowired
    @Qualifier("genreDbStorage")
    private GenreStorage genreDbStorage;

    public List<Genre> findAllGenres() {
        List<Genre> genres = genreDbStorage.findAllGenres();
        log.debug("Обработка запроса GET /genres; Текущее количество жанров: {}",
                genres.size());
        return genres;
    }

    public Genre getGenre(int id) {
        checkGenre(id);
        log.debug("Запрошен жанр c id: {}", id);
        return genreDbStorage.getGenre(id);
    }

    public void checkGenre(int id) {
        if (!genreDbStorage.findAllGenresId().contains(id)) {
            log.debug("жанр не найден!");
            throw new NullObjectException("жанр не найден!");
        }
    }
}
