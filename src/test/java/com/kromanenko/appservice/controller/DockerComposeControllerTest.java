package com.kromanenko.appservice.controller;


import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.kromanenko.appservice.dto.dockercompose.DockerComposeFileConfig;
import com.kromanenko.appservice.dto.dockercompose.DockerComposeServiceConfig;
import com.kromanenko.appservice.dto.dockercompose.PostgreSqlDockerComposeRequest;
import com.kromanenko.appservice.exception.DockerComposeServiceException;
import com.kromanenko.appservice.exception.handler.GlobalExceptionHandler;
import com.kromanenko.appservice.service.GameService;
import com.kromanenko.appservice.service.impl.DockerComposeService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class DockerComposeControllerTest {

  @Mock
  private DockerComposeService dockerComposeService;

  @Mock
  private GameService gameService;

  @InjectMocks
  private DockerComposeController controller;

  @InjectMocks
  private GlobalExceptionHandler exceptionHandler;

  @BeforeEach
  void initialiseRestAssuredMockMvcStandalone() {
    RestAssuredMockMvc.standaloneSetup(controller, exceptionHandler);
  }

  @Test
  void shouldReturnBadRequestWhenCreatingDockerComposeWithInvalidRequest() {
    var invalidRequest = new PostgreSqlDockerComposeRequest();
    invalidRequest.setPort("1234");
    invalidRequest.setUsername("test-user");
    invalidRequest.setPassword("test-password");
    invalidRequest.setDatabase("test-db");
    invalidRequest.setVolume("test-volume");

    given()
        .contentType(JSON)
        .body(invalidRequest)
        .when()
        .post("/api/v1/docker-compose/game-1")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("errors.port", notNullValue())
        .body("errors.username", notNullValue())
        .body("errors.password", notNullValue())
        .body("errors.database", notNullValue())
        .body("errors.volume", notNullValue());
  }

  @Test
  void shouldReturnBadRequestWhenCreatingDockerComposeIfGameDoesNotExist() {
    when(gameService.gameExistsById(anyString())).thenReturn(false);

    given()
        .contentType(JSON)
        .body(createValidRequest())
        .when()
        .post("/api/v1/docker-compose/game-1")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void shouldReturnNoContentIfDockerComposeCreated() {
    when(gameService.gameExistsById(anyString())).thenReturn(true);

    given()
        .contentType(JSON)
        .body(createValidRequest())
        .when()
        .post("/api/v1/docker-compose/game-1")
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void shouldReturnDockerComposeConfigWhenGameExists() {
    String gameId = "existing-game";

    var serviceConfig = new DockerComposeServiceConfig();
    serviceConfig.setPorts(List.of("1234:5432"));
    serviceConfig.setImage("postgres:latest");

    var services = Map.of("postgresql", serviceConfig);
    var expectedConfig = new DockerComposeFileConfig();

    expectedConfig.setServices(services);

    when(gameService.gameExistsById(gameId)).thenReturn(true);
    when(dockerComposeService.getDockerComposeFileConfig(gameId)).thenReturn(expectedConfig);

    given()
        .contentType(JSON)
        .when()
        .get("/api/v1/docker-compose/%s".formatted(gameId))
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("services", not(empty()))
        .body("services.postgresql.ports", equalTo(List.of("1234:5432")))
        .body("services.postgresql.image", equalTo("postgres:latest"));
  }

  @Test
  void shouldReturnBadRequestWhenGettingGameThatDoesNotExist() {
    String gameId = "non-existing-game";
    when(gameService.gameExistsById(gameId)).thenReturn(false);

    given()
        .contentType(JSON)
        .when()
        .get("/api/v1/docker-compose/%s".formatted(gameId))
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("message", equalTo("Game not found"));
  }

  @Test
  void shouldReturnNotFoundWhenDownloadingDockerComposeIfGameDoesNotExist() {
    when(gameService.gameExistsById(anyString())).thenReturn(false);

    given()
        .contentType(JSON)
        .when()
        .get("/api/v1/docker-compose/game-1/download")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void shouldReturnInternalServerErrorWhenDownloadingDockerComposeIfServiceFails() {
    when(gameService.gameExistsById(anyString())).thenReturn(true);
    when(dockerComposeService.getDockerComposeFileInputStream(anyString())).thenThrow(
        DockerComposeServiceException.class);

    given()
        .contentType(JSON)
        .when()
        .get("/api/v1/docker-compose/game-1/download")
        .then()
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @Test
  void shouldReturnOkWhenDownloadingDockerCompose() {
    when(gameService.gameExistsById(anyString())).thenReturn(true);
    when(dockerComposeService.getDockerComposeFileInputStream(anyString())).thenReturn(
        new ByteArrayInputStream("test".getBytes()));

    given()
        .when()
        .get("/api/v1/docker-compose/game-1/download")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  private PostgreSqlDockerComposeRequest createValidRequest() {
    var validRequest = new PostgreSqlDockerComposeRequest();
    validRequest.setPort("1234:5432");
    validRequest.setUsername("dbuser");
    validRequest.setPassword("@Test1234");
    validRequest.setDatabase("test_db");
    validRequest.setVolume("/path:/var/lib/postgresql/data");

    return validRequest;
  }
}