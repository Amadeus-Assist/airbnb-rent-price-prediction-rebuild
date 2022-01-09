package com.columbia.backend.annos;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CityCheckValidator.class)
public @interface CityCheck {
    String message() default "{Request City Name Invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
