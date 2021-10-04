package com.oc.paymybuddy.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordEqualityValidator.class)
public  @interface PasswordEquality {

    String message() default "Password confirm is different from password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}