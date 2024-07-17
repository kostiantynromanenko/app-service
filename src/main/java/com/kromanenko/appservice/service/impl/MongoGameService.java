package com.kromanenko.appservice.service.impl;

import com.kromanenko.appservice.exception.DuplicateGameNameException;
import com.kromanenko.appservice.exception.GameServiceException;
import com.kromanenko.appservice.model.Game;
import com.kromanenko.appservice.repository.GameRepository;
import com.kromanenko.appservice.service.GameService;
import com.mongodb.DuplicateKeyException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoGameService implements GameService {

  private final GameRepository gameRepository;

  @Override
  public Game createGame(String gameName) {
    Game game = new Game();
    game.setName(gameName);

    try {
      return gameRepository.save(game);
    } catch (DuplicateKeyException e) {
      throw new DuplicateGameNameException("Failed to create game", e);
    } catch (Exception e) {
      throw new GameServiceException("Failed to create game", e);
    }
  }

  public boolean gameExistsByName(String gameName) {
    try {
      return gameRepository.findByName(gameName).isPresent();
    } catch (Exception e) {
      throw new GameServiceException("Failed to check if game exists", e);
    }
  }

  public boolean gameExistsById(String gameId) {
    try {
      return gameRepository.findById(gameId).isPresent();
    } catch (Exception e) {
      throw new GameServiceException("Failed to check if game exists", e);
    }
  }

  @Override
  public List<Game> findAllGames() {
    try {
      return gameRepository.findAll();
    } catch (Exception e) {
      throw new GameServiceException("Failed to retrieve games", e);
    }
  }
}
