package com.kromanenko.appservice.service.impl;

import com.kromanenko.appservice.exception.GameAlreadyExistsException;
import com.kromanenko.appservice.exception.GameServiceException;
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

  public boolean isGameExists(String gameName) {
    return gameRepository.findByName(gameName).isPresent();
  }

  @Override
  public Game findGameById(String id) {
    return gameRepository.findById(id).orElse(null);
  }

  @Override
  public Game findGameByName(String name) {
    return gameRepository.findByName(name).orElse(null);
  }

  @Override
  public void deleteGame(String id) {
    gameRepository.deleteById(id);
  }

  @Override
  public List<Game> findAllGames() {
    return gameRepository.findAll();
  }
}
