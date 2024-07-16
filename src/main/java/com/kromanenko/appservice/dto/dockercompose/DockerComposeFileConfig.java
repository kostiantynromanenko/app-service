package com.kromanenko.appservice.dto.dockercompose;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DockerComposeFileConfig {
  private String version;
  private Map<String, DockerComposeServiceConfig> services;
}
