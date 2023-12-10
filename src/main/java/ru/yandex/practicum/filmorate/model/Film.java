package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.annotation.*;

@Data
@AllArgsConstructor
public class Film {

    private int id; // целочисленный идентификатор
    @NotBlank
    private String name; // название
    @Size(min = 0, max = 200)
    @NotNull
    private String description; // описание
    @MinimumDate
    private LocalDate releaseDate; // дата релиза
    @Min(value = 1)
    private int duration; // продолжительность фильма
}
