package com.kromanenko.appservice.exception;

public class GameNotFoundException extends RuntimeException {

  public GameNotFoundException(String message) {
    super(message);
  }
}
