package com.oc.paymybuddy.validation;

import com.oc.paymybuddy.domain.UserForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordEqualityValidator implements ConstraintValidator<PasswordEquality, UserForm> {

    @Override
    public boolean isValid(UserForm userFormDTO, ConstraintValidatorContext context) {

        if ( userFormDTO.getPassword() == null || userFormDTO.getPasswordconfirm() == null ) {
            return false;
        }
        return userFormDTO.getPassword().equals(userFormDTO.getPasswordconfirm());
    }
}