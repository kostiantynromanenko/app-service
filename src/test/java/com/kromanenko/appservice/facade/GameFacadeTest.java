package com.kromanenko.appservice.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kromanenko.appservice.dto.game.CreateGameRequest;
import com.kromanenko.appservice.exception.BucketAlreadyExistsException;
import com.kromanenko.appservice.exception.DuplicateGameNameException;
import com.kromanenko.appservice.exception.GameAlreadyExistsException;
import com.kromanenko.appservice.exception.GameServiceException;
import com.kromanenko.appservice.exception.StorageServiceException;
import com.kromanenko.appservice.model.Game;
import com.kromanenko.appservice.service.GameService;
import com.kromanenko.appservice.service.StorageService;
import com.kromanenko.appservice.service.impl.MinioStorageService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameFacadeTest {

  @Mock
  private GameService gameService;

  @Mock
  private MinioStorageService storageService;

  @InjectMocks
  private GameFacade gameFacade;

  @Test
  void shouldCreateGame() {
    // given
    var request = new CreateGameRequest();
    request.setName("game");

    var game = new Game();
    game.setId("1");
    game.setName(request.getName());

    when(gameService.createGame(request.getName())).thenReturn(game);

    // when
    gameFacade.createGame(request);

    // then
    verify(gameService, times(1)).createGame(request.getName());
    verify(storageService, times(1)).createBucket(game.getId());
  }

  @Test
  void shouldThrowExceptionWhenGameNameAlreadyExists() {
    // given
    var request = new CreateGameRequest();
    request.setName("game");

    when(gameService.createGame(request.getName())).thenThrow(DuplicateGameNameException.class);

    // then
    assertThrows(GameAlreadyExistsException.class, () -> gameFacade.createGame(request));
  }

  @Test
  void shouldThrowExceptionWhenUnexpectedErrorOccurs() {
    // given
    var request = new CreateGameRequest();
    request.setName("game");

    when(gameService.createGame(request.getName())).thenReturn(new Game());
    doThrow(StorageServiceException.class).when(storageService).createBucket(anyString());

    // then
    assertThrows(GameServiceException.class, () -> gameFacade.createGame(request));
  }

  @Test
  void shouldGetAllGames() {
    // given
    var game1 = new Game();
    game1.setId("1");
    game1.setName("game1");

    var game2 = new Game();
    game2.setId("2");
    game2.setName("game2");

    when(gameService.findAllGames()).thenReturn(List.of(game1, game2));

    // when
    var games = gameFacade.getAllGames();

    // then
    assertEquals(2, games.size());
    assertEquals("1", games.get(0).getId());
    assertEquals("game1", games.get(0).getName());
    assertEquals("2", games.get(1).getId());
    assertEquals("game2", games.get(1).getName());
  }
}
