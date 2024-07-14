package com.kromanenko.appservice.service.impl;

import com.kromanenko.appservice.dto.CreateDockerComposeRequest;
import com.kromanenko.appservice.exception.DockerComposeException;
import com.kromanenko.appservice.model.DockerComposeGeneratorConfig;
import com.kromanenko.appservice.util.DockerComposeGenerator;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DockerComposeService {

  private final MinioStorageService minioStorageService;

  public void createDockerCompose(CreateDockerComposeRequest request) {
    var dockerComposeConfig = request.getConfig();

    var generatorConfig = DockerComposeGeneratorConfig.builder()
        .port(dockerComposeConfig.getPort())
        .username(dockerComposeConfig.getUsername())
        .password(dockerComposeConfig.getPassword())
        .volume(dockerComposeConfig.getVolume())
        .build();

    try {
      var dockerComposeContent = DockerComposeGenerator.createDockerComposeContent(generatorConfig);
    } catch (IOException e) {
      throw new DockerComposeException("Failed to create docker compose file", e);
    }
  }
}
