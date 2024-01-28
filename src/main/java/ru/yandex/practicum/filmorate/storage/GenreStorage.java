package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> findAllGenres();

    Genre getGenre(int id);

    List<Integer> findAllGenresId();
}
