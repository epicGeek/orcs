package com.nokia.ices.apps.fusion.system.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameValidator implements ConstraintValidator<NotEqualsIgnore, String> {

    private String compareValue = "";

    @Override
    public void initialize(NotEqualsIgnore constraintAnnotation) {
        compareValue = constraintAnnotation.value();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !compareValue.equalsIgnoreCase(value);
    }
}
