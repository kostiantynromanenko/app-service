package com.kromanenko.appservice.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DockerComposeGeneratorConfig {
  private int port;
  private String username;
  private String password;
  private String volume;
}
