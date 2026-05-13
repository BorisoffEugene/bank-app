package ru.yandex.practicum.accounts.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.accounts.validation.AdultValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdultValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Adult {
    String message() default "Возраст должен быть старше 18 лет";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
