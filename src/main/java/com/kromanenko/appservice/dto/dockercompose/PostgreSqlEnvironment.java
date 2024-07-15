package com.kromanenko.appservice.dto.dockercompose;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;

public class PostgreSqlEnvironment {
  @JsonSetter("POSTGRES_USER")
  @JsonAlias("POSTGRES_USER")
  private String username;
  @JsonSetter("POSTGRES_PASSWORD")
  @JsonAlias("POSTGRES_PASSWORD")
  private String password;
  @JsonSetter("POSTGRES_DB")
  @JsonAlias("POSTGRES_DB")
  private String database;
}
