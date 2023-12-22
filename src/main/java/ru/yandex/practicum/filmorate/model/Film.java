package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Singular;
import ru.yandex.practicum.filmorate.annotation.*;

@Data
@Builder
public class Film implements Comparable<Film> {

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
    @Singular
    private final List<Integer> likes = new ArrayList<>();

    @Override
    public int compareTo(Film film) {
        return film.getLikes().size() - this.getLikes().size();
    }

}
