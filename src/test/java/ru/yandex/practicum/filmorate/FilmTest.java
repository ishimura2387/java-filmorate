package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.Assert.assertEquals;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

import java.time.LocalDate;


@SpringBootTest
public class FilmTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void filmNameTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Терминатор")
                .description("Про роботов")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(120)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name(null)
                .description("Про роботов")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(120)
                .build();
        Film film3 = Film.builder()
                .id(3)
                .name("")
                .description("Про роботов")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(120)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertEquals(violations.size(), 1);
        violations = validator.validate(film1);
        assertEquals(violations.size(), 0);
        violations = validator.validate(film3);
        assertEquals(violations.size(), 1);
    }

    @Test
    public void filmDescriptionTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Терминатор")
                .description("")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(120)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("Терминатор 2")
                .description(null)
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(120)
                .build();
        Film film3 = Film.builder()
                .id(3)
                .name("Терминатор 3")
                .description("Про роботов Про роботов Про роботов Про роботов Про роботов Про роботов Про роботов " +
                        "Про роботов Про роботов Про роботов Про роботов Про роботов Про роботов" +
                        "Про роботов Про роботов Про роботов Про роботов Про роботов Про роботов Про роботов Про роботов")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(120)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertEquals(violations.size(), 1);
        violations = validator.validate(film1);
        assertEquals(violations.size(), 0);
        violations = validator.validate(film3);
        assertEquals(violations.size(), 1);
    }

    @Test
    public void filmDateTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Терминатор")
                .description("Про роботов")
                .releaseDate(LocalDate.of(1690, 12, 20))
                .duration(120)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("Терминатор 2")
                .description("Про роботов")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(120)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertEquals(violations.size(), 0);
        violations = validator.validate(film1);
        assertEquals(violations.size(), 1);
    }

    @Test
    public void filmDurationTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Терминатор")
                .description("Про роботов")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(0)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("Терминатор 2")
                .description("Про роботов")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(120)
                .build();
        Film film3 = Film.builder()
                .id(3)
                .name("Терминатор 3")
                .description("Про роботов")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(-2)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertEquals(violations.size(), 0);
        violations = validator.validate(film1);
        assertEquals(violations.size(), 1);
        violations = validator.validate(film3);
        assertEquals(violations.size(), 1);
    }

}
