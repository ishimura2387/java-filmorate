package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private int id; // целочисленный идентификатор
    private String name; // название
    private String description; // описание
    private LocalDate releaseDate; // дата релиза
    private Duration duration; // продолжительность фильма

}
