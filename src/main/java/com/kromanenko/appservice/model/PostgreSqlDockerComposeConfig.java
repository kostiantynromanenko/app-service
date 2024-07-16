package com.kromanenko.appservice.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostgreSqlDockerComposeConfig {

  private String username;
  private String password;
  private String database;
  private String volume;
  private String port;
}
