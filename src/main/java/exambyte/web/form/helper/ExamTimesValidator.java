package exambyte.web.form.helper;

import exambyte.web.form.ExamForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExamTimesValidator implements ConstraintValidator<ValidExamTimes, ExamForm> {

    @Override
    public boolean isValid(ExamForm form, ConstraintValidatorContext context) {
        if (form.getStart() == null || form.getEnd() == null || form.getResult() == null) {
            return false;
        }

        return form.getStart().isBefore(form.getEnd())
                && (form.getEnd().isBefore(form.getResult()));
    }
}
