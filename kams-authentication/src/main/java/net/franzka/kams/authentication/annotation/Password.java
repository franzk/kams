package net.franzka.kams.authentication.annotation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Annotation @Password
 * Constraint for password :
 *  must have at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordConstraintsValidator.class)
public @interface Password {

    String message() default "Invalid password!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}