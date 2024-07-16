package com.kromanenko.appservice.exception;

public class BucketNotFoundException extends RuntimeException {

  public BucketNotFoundException(String message) {
    super(message);
  }
}
