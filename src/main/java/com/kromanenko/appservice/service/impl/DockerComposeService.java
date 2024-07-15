package com.kromanenko.appservice.service.impl;

import com.kromanenko.appservice.dto.dockercompose.CreateDockerComposeRequest;
import com.kromanenko.appservice.exception.DockerComposeServiceException;
import com.kromanenko.appservice.model.DockerComposeConfig;
import com.kromanenko.appservice.service.GameService;
import com.kromanenko.appservice.service.StorageService;
import com.kromanenko.appservice.util.DockerComposeGenerator;
import java.io.ByteArrayInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DockerComposeService {

  private static final String DOCKER_COMPOSE_FILE_NAME = "docker-compose.yml";
  private static final String YAML_CONTENT_TYPE = "application/x-yaml";

  private final StorageService storageService;
  private final GameService gameService;

  public void createDockerCompose(CreateDockerComposeRequest request) {
    var generatorConfig = createGeneratorConfig(request);

    try {
      var dockerComposeContent = DockerComposeGenerator.createDockerComposeContent(generatorConfig);
      var contentInputStream = new ByteArrayInputStream(dockerComposeContent.getBytes());

      storageService.uploadFile("game1", DOCKER_COMPOSE_FILE_NAME, contentInputStream,
          YAML_CONTENT_TYPE);
    } catch (Exception e) {
      throw new DockerComposeServiceException("Failed to create docker compose file", e);
    }
  }

  private DockerComposeConfig createGeneratorConfig(CreateDockerComposeRequest request) {
    var dockerComposeConfig = request.getConfig();

    return DockerComposeConfig.builder()
        .port(dockerComposeConfig.getPort())
        .username(dockerComposeConfig.getUsername())
        .password(dockerComposeConfig.getPassword())
        .volume(dockerComposeConfig.getVolume())
        .build();
  }
}
