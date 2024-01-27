package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.TreeSet;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @Builder.Default
    private Integer likes = 0;
    private Mpa mpa;
    private TreeSet<Genre> genres = new TreeSet<>();

    @Override
    public int compareTo(Film film) {
        return film.getLikes() - this.getLikes();
    }

}
