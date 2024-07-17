package com.kromanenko.appservice.exception;

public class DuplicateGameNameException extends RuntimeException {

  public DuplicateGameNameException(String message, Throwable cause) {
    super(message, cause);
  }
}
