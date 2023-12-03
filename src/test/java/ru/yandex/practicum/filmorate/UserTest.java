package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class UserTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void userMailTest() {
        User user1 = new User(1, "user1@yandex.ru", "user1", "Петя", LocalDate.of(2000, 5, 11));
        User user2 = new User(2, "user2yandex.ru", "user2", "Вася", LocalDate.of(2000, 6, 12));
        User user3 = new User(3, "useryandex.ru@", "user3", "Шахоб", LocalDate.of(1997, 2, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user2);
        assertEquals(violations.size(), 1);
        violations = validator.validate(user1);
        assertEquals(violations.size(), 0);
        violations = validator.validate(user3);
        assertEquals(violations.size(), 1);
    }

    @Test
    public void userLoginTest() {
        User user1 = new User(1, "user1@yandex.ru", "user1", "Петя", LocalDate.of(2000, 5, 11));
        User user2 = new User(2, "user2@yandex.ru", "", "Вася", LocalDate.of(2000, 6, 12));
        User user3 = new User(3, "user3@yandex.ru", null, "Шахоб", LocalDate.of(1997, 2, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user2);
        assertEquals(violations.size(), 1);
        violations = validator.validate(user1);
        assertEquals(violations.size(), 0);
        violations = validator.validate(user3);
        assertEquals(violations.size(), 1);
    }

    @Test
    public void userDateBirthdayTest() {
        User user1 = new User(1, "user1@yandex.ru", "user1", "Петя", LocalDate.of(2024, 5, 11));
        User user2 = new User(2, "user2@yandex.ru", "user2", "Вася", LocalDate.of(2000, 6, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertEquals(violations.size(), 1);
        violations = validator.validate(user2);
        assertEquals(violations.size(), 0);
    }

}
