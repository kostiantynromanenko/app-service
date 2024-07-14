package com.kromanenko.appservice.util;

public final class ValidationPatterns {

  private ValidationPatterns() {
  }

  public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$";
  public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";
  public static final String VOLUME_PATTERN = "^\\/[^\\0]*$";
}
