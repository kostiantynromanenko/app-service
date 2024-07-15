package com.kromanenko.appservice.dto.dockercompose;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

public class PostgreSqlService {
  @JsonSetter("container_name")
  @JsonAlias("container_name")
  private String containerName;
  private String image;
  private List<String> ports;
  private PostgreSqlEnvironment environment;
  private List<String> volumes;

}
