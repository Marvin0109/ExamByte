package exambyte.web.form.helper;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExamTimesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExamTimes {
    String message() default "Startzeit muss vor der Endzeit liegen und Endzeit darf nicht nach der Ergebniszeit sein!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
