package com.kromanenko.appservice.validation;

import static java.util.Objects.isNull;

import com.kromanenko.appservice.validation.annotations.ValidPort;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PortValidator implements ConstraintValidator<ValidPort, Integer> {

  @Override
  public boolean isValid(Integer port, ConstraintValidatorContext constraintValidatorContext) {
    if (isNull(port)) {
      return false;
    }
    return port >= 0 && port <= 65535;
  }
}
