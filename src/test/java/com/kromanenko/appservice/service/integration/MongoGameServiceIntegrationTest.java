package com.kromanenko.appservice.service.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kromanenko.appservice.model.Game;
import com.kromanenko.appservice.repository.GameRepository;
import com.kromanenko.appservice.service.impl.MongoGameService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Import(MongoGameService.class)
@Testcontainers
public class MongoGameServiceIntegrationTest {

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

  @Autowired
  private MongoGameService gameService;

  @Autowired
  private GameRepository gameRepository;

  @DynamicPropertySource
  static void mongoDbProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  @BeforeAll
  static void setup() {
    mongoDBContainer.start();
  }

  @AfterAll
  static void tearDown() {
    mongoDBContainer.stop();
  }

  @Test
  void shouldCreateGame() {
    // given
    String gameName = "Test Game";

    // when
    var game = gameService.createGame(gameName);

    // then
    assertNotNull(game);
    assertTrue(gameRepository.findByName(gameName).isPresent());
  }

  @Test
  void shouldFindAllGames() {
    // given
    var game1 = new Game();
    game1.setName("Test Game 1");
    gameRepository.save(game1);

    var game2 = new Game();
    game2.setName("Test Game 2");
    gameRepository.save(game2);

    // when
    var games = gameService.findAllGames();

    // then
    assertEquals(2, gameRepository.findAll().size());
    assertEquals(game1.getName(), games.get(0).getName());
    assertEquals(game2.getName(), games.get(1).getName());
  }

  @Test
  void shouldCheckGameExistsByName() {
    // given
    String gameName = "Test Game 3";
    gameService.createGame(gameName);

    // then
    assertTrue(gameService.gameExistsByName(gameName));
  }

  @Test
  void shouldCheckGameExistsByBy() {
    // given
    String gameName = "Test Game 4";

    var game = gameService.createGame(gameName);

    // then
    assertNotNull(game);
    assertTrue(gameService.gameExistsById(game.getId()));
  }
}
