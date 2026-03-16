package exambyte.web.form.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HalfPointsAboveZeroValidator implements ConstraintValidator<HalfPointsAboveZero, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return (value * 2) % 1 == 0;
    }
}
