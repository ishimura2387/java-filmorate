package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.CheckSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.TreeSet;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private int id; // целочисленный идентификатор
    @Email
    private String email; // электронная почта
    @NotBlank
    @CheckSpace
    private String login; // логин пользователя
    private String name; // имя для отображения
    @Past
    private LocalDate birthday; // дата рождения
    @JsonIgnore
    private final TreeSet<Integer> friends = new TreeSet<>();
}
