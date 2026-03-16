package exambyte.web.form.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HalfPointsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HalfPoints {
    String message() default "Nur halbe Punkte erlaubt (0.5 Schritte)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
