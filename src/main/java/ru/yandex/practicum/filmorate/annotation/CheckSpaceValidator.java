package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckSpaceValidator implements ConstraintValidator<CheckSpace, String> {
    private String space;

    @Override
    public void initialize(CheckSpace constraintAnnotation) {
        space = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return !value.contains(space);
    }
}
