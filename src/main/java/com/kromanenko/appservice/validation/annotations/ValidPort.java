package com.kromanenko.appservice.validation.annotations;

import com.kromanenko.appservice.validation.PortValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PortValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPort {

  String message() default "Invalid port number";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
