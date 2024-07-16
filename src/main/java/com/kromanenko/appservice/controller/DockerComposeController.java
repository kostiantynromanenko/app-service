package com.kromanenko.appservice.controller;

import static com.kromanenko.appservice.util.DockerComposeConstants.DOWNLOAD_CONTENT_DISPOSITION;

import com.kromanenko.appservice.dto.dockercompose.DockerComposeFileConfig;
import com.kromanenko.appservice.dto.dockercompose.PostgreSqlDockerComposeRequest;
import com.kromanenko.appservice.exception.DockerComposeServiceException;
import com.kromanenko.appservice.exception.GameNotFoundException;
import com.kromanenko.appservice.service.GameService;
import com.kromanenko.appservice.service.impl.DockerComposeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/docker-compose")
@RequiredArgsConstructor
public class DockerComposeController {

  private final DockerComposeService dockerComposeService;
  private final GameService gameService;

  @PostMapping("/{gameId}")
  public ResponseEntity<String> saveDockerCompose(@PathVariable String gameId,
      @Valid @RequestBody PostgreSqlDockerComposeRequest request) {
    if (!gameService.gameExistsById(gameId)) {
      throw new GameNotFoundException("Game not found");
    }

    dockerComposeService.createPostgreSqlDockerCompose(gameId, request);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{gameId}/download")
  public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String gameId) {
    if (!gameService.gameExistsById(gameId)) {
      throw new GameNotFoundException("Game not found");
    }

    try {
      var fileInputStream = dockerComposeService.getDockerComposeFileInputStream(gameId);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, DOWNLOAD_CONTENT_DISPOSITION)
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(new InputStreamResource(fileInputStream));

    } catch (DockerComposeServiceException e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @GetMapping("/{gameId}")
  public DockerComposeFileConfig getDockerComposeConfig(@PathVariable String gameId) {
    if (!gameService.gameExistsById(gameId)) {
      throw new GameNotFoundException("Game not found");
    }

    return dockerComposeService.getDockerComposeFileConfig(gameId);
  }

  @DeleteMapping("/{gameId}")
  public ResponseEntity<Void> deleteDockerCompose(@PathVariable String gameId) {
    if (!gameService.gameExistsById(gameId)) {
      throw new GameNotFoundException("Game not found");
    }

    dockerComposeService.deleteDockerComposeFile(gameId);
    return ResponseEntity.noContent().build();
  }
}
