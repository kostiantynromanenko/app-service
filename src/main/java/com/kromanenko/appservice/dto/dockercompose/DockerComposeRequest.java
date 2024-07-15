package com.kromanenko.appservice.dto.dockercompose;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DockerComposeRequest {

  @NotBlank(message = "Game name is required")
  private String gameName;

  @NotNull(message = "Docker compose config is required")
  @Valid
  private DockerComposeConfigDto config;
}
