package com.kromanenko.appservice.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DockerComposeConfig {

  private int port;
  private String username;
  private String password;
  private String volume;
}
