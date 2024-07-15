package com.kromanenko.appservice.exception;

public class DockerComposeFileNotFound extends RuntimeException {

  public DockerComposeFileNotFound(String message) {
    super(message);
  }
}
