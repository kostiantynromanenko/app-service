package com.kromanenko.appservice.exception;

public class DockerComposeException extends RuntimeException {

  public DockerComposeException(String message, Throwable cause) {
    super(message, cause);
  }
}
