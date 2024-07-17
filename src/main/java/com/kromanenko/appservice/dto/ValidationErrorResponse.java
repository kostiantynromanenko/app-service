package com.kromanenko.appservice.dto;

import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse extends ErrorResponse {

  private Map<String, String> errors;

  public ValidationErrorResponse(int status, String message, long timestamp,
      Map<String, String> errors) {
    super(status, message, timestamp);
    this.errors = errors;
  }
}
