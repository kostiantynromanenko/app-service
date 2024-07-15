package com.kromanenko.appservice.facade;

import static java.util.stream.Collectors.toList;

import com.kromanenko.appservice.dto.game.CreateGameRequest;
import com.kromanenko.appservice.dto.game.GameResponse;
import com.kromanenko.appservice.exception.GameAlreadyExistsException;
import com.kromanenko.appservice.service.GameService;
import com.kromanenko.appservice.service.StorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameFacade {

  private final GameService gameService;
  private final StorageService storageService;

  public GameResponse createGame(CreateGameRequest request) {
    if (gameService.isGameExists(request.getName())) {
      throw new GameAlreadyExistsException(
          "Game with name %s already exists".formatted(request.getName()));
    }

    var game = gameService.createGame(request.getName());
    storageService.createBucket(game.getId());
    return new GameResponse(game.getId(), game.getName());
  }

  public List<GameResponse> getAllGames() {
    return gameService.findAllGames().stream()
        .map(game -> new GameResponse(game.getId(), game.getName()))
        .collect(toList());
  }
}
