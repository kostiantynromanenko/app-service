package com.kromanenko.appservice.exception;

public class GameAlreadyExistsException extends RuntimeException {

  public GameAlreadyExistsException(String message) {
    super(message);
  }
}
