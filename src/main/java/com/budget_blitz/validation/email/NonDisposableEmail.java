package com.budget_blitz.validation.email;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DisposableEmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonDisposableEmail {

    String message() default "Disposable emails are not allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}