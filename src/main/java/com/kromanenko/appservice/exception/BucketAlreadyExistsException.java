package com.kromanenko.appservice.exception;

public class BucketAlreadyExistsException extends RuntimeException {

  public BucketAlreadyExistsException(String message) {
    super(message);
  }
}
