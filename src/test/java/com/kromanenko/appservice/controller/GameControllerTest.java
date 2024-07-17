package com.kromanenko.appservice.controller;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import com.kromanenko.appservice.dto.game.CreateGameRequest;
import com.kromanenko.appservice.dto.game.GameResponse;
import com.kromanenko.appservice.exception.GameAlreadyExistsException;
import com.kromanenko.appservice.exception.GameServiceException;
import com.kromanenko.appservice.exception.handler.GlobalExceptionHandler;
import com.kromanenko.appservice.facade.GameFacade;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

  @Mock
  private GameFacade gameFacade;

  @InjectMocks
  private GameController controller;

  @InjectMocks
  private GlobalExceptionHandler exceptionHandler;

  @BeforeEach
  void initialiseRestAssuredMockMvcStandalone() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
        new ErrorLoggingFilter());
    RestAssuredMockMvc.standaloneSetup(controller, exceptionHandler);
  }

  @Test
  void shouldReturnConflictWhenCreatingGameWithExistingName() {
    var request = createGameRequest("game");
    when(gameFacade.createGame(request)).thenThrow(new GameAlreadyExistsException(
        "Game with name %s already exists".formatted(request.getName())));

    given()
        .contentType(JSON)
        .body(request)
        .when()
        .post("/api/v1/games")
        .then()
        .statusCode(HttpStatus.CONFLICT.value());
  }

  @Test
  void shouldReturnOkWhenGetAllGames() {
    when(gameFacade.getAllGames()).thenReturn(List.of(new GameResponse("id", "game")));

    given()
        .when()
        .get("/api/v1/games")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("[0].id", equalTo("id"))
        .body("[0].name", equalTo("game"));
  }

  @Test
  void shouldReturnInternalServerErrorWhenFailedToCreateGame() {
    var request = createGameRequest("game1");
    when(gameFacade.createGame(request)).thenThrow(
        new GameServiceException("Failed to create game"));

    given()
        .contentType(JSON)
        .body(request)
        .when()
        .post("/api/v1/games")
        .then()
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @Test
  void shouldReturnOkWhenGameCreated() {
    var request = createGameRequest("game2");
    when(gameFacade.createGame(request)).thenReturn(new GameResponse("id", request.getName()));

    given()
        .contentType(JSON)
        .body(request)
        .when()
        .post("/api/v1/games")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo("id"))
        .body("name", equalTo(request.getName()));
  }

  private CreateGameRequest createGameRequest(String name) {
    var request = new CreateGameRequest();
    request.setName(name);
    return request;
  }
}
