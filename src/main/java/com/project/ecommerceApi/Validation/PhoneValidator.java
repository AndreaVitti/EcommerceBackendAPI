package com.project.ecommerceApi.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneIsValid, Object> {
    @Override
    public void initialize(PhoneIsValid phoneIsValid) {
        ConstraintValidator.super.initialize(phoneIsValid);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            try {
                Long.parseLong(String.valueOf(value));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
