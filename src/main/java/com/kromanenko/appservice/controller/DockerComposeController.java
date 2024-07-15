package com.kromanenko.appservice.controller;

import com.kromanenko.appservice.dto.dockercompose.CreateDockerComposeRequest;
import com.kromanenko.appservice.dto.dockercompose.DockerComposeFile;
import com.kromanenko.appservice.service.impl.DockerComposeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/docker-compose")
@RequiredArgsConstructor
public class DockerComposeController {

  private final DockerComposeService dockerComposeService;

  @PostMapping("/{gameId}")
  public ResponseEntity<String> createDockerCompose(@PathVariable String gameId,
      @Valid @RequestBody CreateDockerComposeRequest request) {
    dockerComposeService.createDockerCompose(gameId, request);
    return ResponseEntity.ok("Docker compose file created successfully");
  }

  @GetMapping("/{gameId}")
  public DockerComposeFile getDockerCompose(@PathVariable String gameId) {
    return dockerComposeService.getDockerComposeFile(gameId);
  }
}
