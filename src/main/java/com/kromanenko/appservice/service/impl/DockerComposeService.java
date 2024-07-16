package com.kromanenko.appservice.service.impl;

import static com.kromanenko.appservice.util.DockerComposeConstants.DOCKER_COMPOSE_FILE_NAME;
import static com.kromanenko.appservice.util.DockerComposeConstants.YAML_CONTENT_TYPE;

import com.kromanenko.appservice.dto.dockercompose.DockerComposeFileConfig;
import com.kromanenko.appservice.dto.dockercompose.PostgreSqlDockerComposeRequest;
import com.kromanenko.appservice.exception.BucketNotFoundException;
import com.kromanenko.appservice.exception.DockerComposeFileNotFound;
import com.kromanenko.appservice.exception.DockerComposeServiceException;
import com.kromanenko.appservice.model.PostgreSqlDockerComposeConfig;
import com.kromanenko.appservice.service.StorageService;
import com.kromanenko.appservice.util.DockerComposeParser;
import com.kromanenko.appservice.util.PostgreSqlDockerComposeGenerator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DockerComposeService {

  private final StorageService storageService;

  public void createPostgreSqlDockerCompose(String bucketName,
      PostgreSqlDockerComposeRequest request) {
    if (!storageService.bucketExists(bucketName)) {
      throw new BucketNotFoundException("Bucket does not exist");
    }

    var config = createPostreSqlDockerComposeConfig(request);

    try {
      var dockerComposeContent = PostgreSqlDockerComposeGenerator.createDockerComposeContent(
          config);
      var contentInputStream = new ByteArrayInputStream(dockerComposeContent.getBytes());

      storageService.uploadFile(bucketName, DOCKER_COMPOSE_FILE_NAME, contentInputStream,
          YAML_CONTENT_TYPE);
    } catch (Exception e) {
      throw new DockerComposeServiceException("Failed to create docker compose file", e);
    }
  }

  public InputStream getDockerComposeFileInputStream(String bucketName) {
    if (!storageService.bucketExists(bucketName)) {
      throw new BucketNotFoundException("Bucket does not exist");
    }

    if (!storageService.fileExists(bucketName, DOCKER_COMPOSE_FILE_NAME)) {
      throw new DockerComposeFileNotFound("Docker compose file not found");
    }

    try {
      return storageService.getFileInputStream(bucketName, DOCKER_COMPOSE_FILE_NAME);
    } catch (Exception e) {
      throw new DockerComposeServiceException("Failed to parse docker compose file", e);
    }
  }

  public DockerComposeFileConfig getDockerComposeFileConfig(String bucketName) {
    var inputStream = getDockerComposeFileInputStream(bucketName);

    try {
      return DockerComposeParser.parseDockerComposeFile(inputStream);
    } catch (Exception e) {
      throw new DockerComposeServiceException("Failed to parse docker compose file", e);
    }
  }

  public void deleteDockerComposeFile(String bucketName) {
    if (!storageService.bucketExists(bucketName)) {
      throw new BucketNotFoundException("Bucket does not exist");
    }

    try {
      storageService.deleteFile(bucketName, DOCKER_COMPOSE_FILE_NAME);
    } catch (Exception e) {
      throw new DockerComposeServiceException("Failed to delete docker compose file", e);
    }
  }

  private PostgreSqlDockerComposeConfig createPostreSqlDockerComposeConfig(
      PostgreSqlDockerComposeRequest request) {
    return PostgreSqlDockerComposeConfig.builder()
        .port(request.getPort())
        .username(request.getUsername())
        .password(request.getPassword())
        .database(request.getDatabase())
        .volume(request.getVolume())
        .build();
  }
}
