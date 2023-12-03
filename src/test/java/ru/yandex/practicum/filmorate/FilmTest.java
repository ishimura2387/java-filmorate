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
        Film film1 = new Film(1, "Терминатор", "Про роботов", LocalDate.of(1990, 12, 20), 120);
        Film film2 = new Film(2, null, "Про роботов", LocalDate.of(1990, 12, 20), 120);
        Film film3 = new Film(3, "", "Про роботов", LocalDate.of(1990, 12, 20), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertEquals(violations.size(), 1);
        violations = validator.validate(film1);
        assertEquals(violations.size(), 0);
        violations = validator.validate(film3);
        assertEquals(violations.size(), 1);
    }
}
