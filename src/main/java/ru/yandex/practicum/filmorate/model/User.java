package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.CheckSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
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

}
