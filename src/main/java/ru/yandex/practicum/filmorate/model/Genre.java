package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Genre implements Comparable<Genre> {
    private int id;
    private final String name;

    @Override
    public int compareTo(Genre genre) {
        return this.getId() - genre.getId();
    }
}
