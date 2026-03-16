package exambyte.web.form.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = QuestionValidator.class)
public @interface ValidQuestion {
    String message() default "Ungültige Frage";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
