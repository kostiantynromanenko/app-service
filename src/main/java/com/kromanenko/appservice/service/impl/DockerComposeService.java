package com.kromanenko.appservice.service.impl;

import com.kromanenko.appservice.dto.dockercompose.CreateDockerComposeRequest;
import com.kromanenko.appservice.dto.dockercompose.DockerComposeFile;
import com.kromanenko.appservice.exception.DockerComposeFileNotFound;
import com.kromanenko.appservice.exception.DockerComposeServiceException;
import com.kromanenko.appservice.exception.GameNotFoundException;
import com.kromanenko.appservice.exception.GameServiceException;
import com.kromanenko.appservice.exception.StorageServiceException;
import com.kromanenko.appservice.facade.GameFacade;
import com.kromanenko.appservice.model.DockerComposeConfig;
import com.kromanenko.appservice.service.GameService;
import com.kromanenko.appservice.service.StorageService;
import com.kromanenko.appservice.util.DockerComposeGenerator;
import com.kromanenko.appservice.util.DockerComposeParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DockerComposeService {

  private static final String DOCKER_COMPOSE_FILE_NAME = "docker-compose.yml";
  private static final String YAML_CONTENT_TYPE = "application/x-yaml";

  private final StorageService storageService;
  private final GameService gameService;

  public void createDockerCompose(String bucketName, CreateDockerComposeRequest request) {
    var generatorConfig = createGeneratorConfig(request);

    try {
      var dockerComposeContent = DockerComposeGenerator.createDockerComposeContent(generatorConfig);
      var contentInputStream = new ByteArrayInputStream(dockerComposeContent.getBytes());

      storageService.uploadFile(bucketName, DOCKER_COMPOSE_FILE_NAME, contentInputStream,
          YAML_CONTENT_TYPE);
    } catch (Exception e) {
      throw new DockerComposeServiceException("Failed to create docker compose file", e);
    }
  }

  public DockerComposeFile getDockerComposeFile(String gameId) {
    if (!gameService.gameExistsById(gameId)) {
      throw new GameNotFoundException("Game not found");
    }
    if (!storageService.fileExists(gameId, DOCKER_COMPOSE_FILE_NAME)) {
      throw new DockerComposeFileNotFound("Docker compose file not found");
    }

    try {
      var inputStream = storageService.getFileInputStream(gameId, DOCKER_COMPOSE_FILE_NAME);
      return DockerComposeParser.parseDockerComposeFile(inputStream);
    } catch (Exception e) {
      throw new DockerComposeServiceException("Failed to parse docker compose file", e);
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
