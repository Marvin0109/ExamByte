package exambyte.web.form;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HalfPointsValidator implements ConstraintValidator<HalfPoints, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return (value * 2) % 1 == 0;
    }
}
