package com.akul.dsp.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;

public class CapitalAndNumberValidator implements ConstraintValidator<CapitalAndNumber, String> {
    @Override
    public void initialize(CapitalAndNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Strings.isEmpty(value)) return false;

        String capitalRegex = ".*[A-Z].*";
        String numberRegex = ".*\\d.*";

        return value.matches(capitalRegex) && value.matches(numberRegex);
    }
}
