package exambyte.web.form.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HalfPointsValidator implements ConstraintValidator<HalfPoints, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;
        if (value < 0.5) return true;
        return (value * 2) % 1 == 0;
    }
}
