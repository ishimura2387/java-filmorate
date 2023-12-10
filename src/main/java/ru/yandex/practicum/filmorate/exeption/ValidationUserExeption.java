package ru.yandex.practicum.filmorate.exeption;

public class ValidationUserExeption extends RuntimeException {

    public ValidationUserExeption(final String message) {
        super(message);
    }

}
