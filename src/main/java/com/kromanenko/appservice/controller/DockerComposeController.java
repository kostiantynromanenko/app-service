package com.kromanenko.appservice.controller;

import com.kromanenko.appservice.dto.dockercompose.CreateDockerComposeRequest;
import com.kromanenko.appservice.service.impl.DockerComposeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/docker-compose")
@RequiredArgsConstructor
public class DockerComposeController {

  private final DockerComposeService dockerComposeService;

  @PostMapping
  public ResponseEntity<String> createDockerCompose(
      @Valid @RequestBody CreateDockerComposeRequest request) {
    dockerComposeService.createDockerCompose(request);
    return ResponseEntity.ok("Docker compose file created successfully");
  }
}
