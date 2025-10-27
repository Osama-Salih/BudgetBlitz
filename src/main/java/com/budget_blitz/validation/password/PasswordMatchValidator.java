package com.budget_blitz.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {

            try {
                final Method getPassword = obj.getClass().getMethod("getPassword");
                final Method getConfirmPassword = obj.getClass().getMethod("getConfirmPassword");

                final String password = (String) getPassword.invoke(obj);
                final String confirmPassword = (String) getConfirmPassword.invoke(obj);

                if (password == null || confirmPassword == null) {
                    return true;
                }

                final boolean matches = password.equals(confirmPassword);
                if (!matches) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Confirm password and new password mismatch")
                            .addPropertyNode("confirmPassword")
                            .addConstraintViolation();
                }
                return matches;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return false;
            }
    }
}
