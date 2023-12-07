package ru.yandex.practicum.filmorate.exeption;

public class ValidationFilmExeption extends RuntimeException {

    public ValidationFilmExeption(final String message) {
        super(message);
    }

}
