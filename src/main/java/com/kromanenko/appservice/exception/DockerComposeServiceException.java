package com.kromanenko.appservice.exception;

public class DockerComposeServiceException extends RuntimeException {

  public DockerComposeServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
