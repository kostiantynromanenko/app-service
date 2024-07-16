package com.kromanenko.appservice.util;

public final class DockerComposeConstants {

  private DockerComposeConstants() {
  }

  public static final String DOCKER_COMPOSE_FILE_NAME = "docker-compose.yml";
  public static final String YAML_CONTENT_TYPE = "application/x-yaml";
  public static final String DOWNLOAD_CONTENT_DISPOSITION =
      "attachment; filename=\"%s\"".formatted(DOCKER_COMPOSE_FILE_NAME);

  public static final String PORT = "port";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String DATABASE = "database";
  public static final String VOLUME = "volume";
}
