package com.kromanenko.appservice.dto.dockercompose;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DockerComposeRequest {
  @NotNull(message = "Docker compose config is required")
  @Valid
  private DockerComposeConfigDto config;
}
