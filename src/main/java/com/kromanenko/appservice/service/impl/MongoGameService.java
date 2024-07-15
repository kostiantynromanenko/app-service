package com.kromanenko.appservice.service.impl;

import com.kromanenko.appservice.model.Game;
import com.kromanenko.appservice.repository.GameRepository;
import com.kromanenko.appservice.service.GameService;
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
    return gameRepository.save(game);
  }

  public boolean gameExistsByName(String gameName) {
    return gameRepository.findByName(gameName).isPresent();
  }

  public boolean gameExistsById(String gameId) {
    return gameRepository.findById(gameId).isPresent();
  }

  @Override
  public List<Game> findAllGames() {
    return gameRepository.findAll();
  }
}
