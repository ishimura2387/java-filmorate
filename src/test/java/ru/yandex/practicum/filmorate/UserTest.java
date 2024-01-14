package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
        User user1 = User.builder()
                .id(1)
                .email("user1@yandex.ru")
                .login("user1")
                .name("Петя")
                .birthday(LocalDate.of(2000, 5, 11))
                .build();
        User user2 = User.builder()
                .id(2)
                .email("user2yandex.ru")
                .login("user2")
                .name("Вася")
                .birthday(LocalDate.of(2000, 6, 12))
                .build();
        User user3 = User.builder()
                .id(3)
                .email("useryandex.ru@")
                .login("user3")
                .name("Шахоб")
                .birthday(LocalDate.of(1997, 2, 20))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user2);
        assertEquals(violations.size(), 1);
        violations = validator.validate(user1);
        assertEquals(violations.size(), 0);
        violations = validator.validate(user3);
        assertEquals(violations.size(), 1);
    }

    @Test
    public void userDateBirthdayTest() {
        User user1 = User.builder()
                .id(1)
                .email("user1@yandex.ru")
                .login("user1")
                .name("Петя")
                .birthday(LocalDate.of(2024, 5, 11))
                .build();
        User user2 = User.builder()
                .id(2)
                .email("user2@yandex.ru")
                .login("user2")
                .name("Вася")
                .birthday(LocalDate.of(2000, 6, 12))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertEquals(violations.size(), 1);
        violations = validator.validate(user2);
        assertEquals(violations.size(), 0);
    }

    @Test
    public void userLoginTest() {
        User user1 = User.builder()
                .id(1)
                .email("user1@yandex.ru")
                .login(null)
                .name("Петя")
                .birthday(LocalDate.of(2021, 5, 11))
                .build();
        User user2 = User.builder()
                .id(2)
                .email("user2@yandex.ru")
                .login("user2")
                .name("Вася")
                .birthday(LocalDate.of(2000, 6, 12))
                .build();
        User user3 = User.builder()
                .id(3)
                .email("user1@yandex.ru")
                .login("user 1")
                .name("Петя")
                .birthday(LocalDate.of(2021, 5, 11))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertEquals(violations.size(), 2);
        violations = validator.validate(user2);
        assertEquals(violations.size(), 0);
        violations = validator.validate(user3);
        assertEquals(violations.size(), 1);
    }

}
