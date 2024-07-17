package com.kromanenko.appservice.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kromanenko.appservice.exception.DuplicateGameNameException;
import com.kromanenko.appservice.exception.GameServiceException;
import com.kromanenko.appservice.model.Game;
import com.kromanenko.appservice.repository.GameRepository;
import com.kromanenko.appservice.service.impl.MongoGameService;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MongoGameServiceTest {

  @Mock
  private GameRepository gameRepository;

  @InjectMocks
  private MongoGameService mongoGameService;

  @Test
  void shouldCreateGame() {
    // given
    Game game = new Game();
    game.setName("Test Game");

    when(gameRepository.save(any(Game.class))).thenReturn(game);

    // when
    Game createdGame = mongoGameService.createGame("Test Game");

    // then
    verify(gameRepository, times(1)).save(any(Game.class));
    assertEquals("Test Game", createdGame.getName());
  }

  @Test
  void shouldThrowExceptionWhenCreatingGameWithExistingName() {
    when(gameRepository.save(any(Game.class))).thenThrow(DuplicateKeyException.class);

    assertThrows(DuplicateGameNameException.class, () -> mongoGameService.createGame("Test Game"));
  }

  @Test
  void shouldThrowExceptionWhenDbIssueOccurs() {
    when(gameRepository.findAll()).thenThrow(MongoException.class);

    assertThrows(GameServiceException.class, () -> mongoGameService.findAllGames());
  }

  @Test
  void shouldReturnsTrueWhenGameExistsByName() {
    when(gameRepository.findByName("Existing Game")).thenReturn(Optional.of(new Game()));
    assertTrue(mongoGameService.gameExistsByName("Existing Game"));
  }

  @Test
  void shouldReturnFalseWhenGameDoesNotExistByName() {
    when(gameRepository.findByName("Nonexistent Game")).thenReturn(Optional.empty());
    assertFalse(mongoGameService.gameExistsByName("Nonexistent Game"));
  }

  @Test
  void shouldReturnTrueWhenGameExistsById() {
    when(gameRepository.findById("existingId")).thenReturn(Optional.of(new Game()));
    assertTrue(mongoGameService.gameExistsById("existingId"));
  }

  @Test
  void shouldReturnFalseWhenGameNotExistsById() {
    when(gameRepository.findById("nonexistentId")).thenReturn(Optional.empty());
    assertFalse(mongoGameService.gameExistsById("nonexistentId"));
  }
}
