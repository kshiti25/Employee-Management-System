package com.csye6220.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = SpecialCharactersValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpecialCharacter {
    String message() default "Input should not contain special characters or numbers";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

