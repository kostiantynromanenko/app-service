package com.kromanenko.appservice.dto.dockercompose;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DockerComposeServiceConfig {

  @JsonSetter("container_name")
  @JsonAlias("container_name")
  private String containerName;
  private String image;
  private List<String> ports;
  private Map<String, String> environment;
  private List<String> volumes;
}
