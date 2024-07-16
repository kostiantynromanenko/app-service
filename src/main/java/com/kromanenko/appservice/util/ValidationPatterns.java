package com.kromanenko.appservice.util;

public final class ValidationPatterns {

  private ValidationPatterns() {
  }

  public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$";
  public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";
  public static final String DATABASE_NAME_PATTERN = "^[a-zA-Z0-9_]+$";
  public static final String POSTGRESQL_VOLUME_PATTERN = "^/[^:]+:/var/lib/postgresql/data$";
  public static final String PORT_PATTERN = "^([1-9][0-9]{3}|[1-5][0-9]{3,4}|6[0-4][0-9]{3}|65" +
      "[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5]):([1-9][0-9]{3}|[1-5][0-9]{3,4}|6[0-4][0-9]{3}|65" +
      "[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
}
