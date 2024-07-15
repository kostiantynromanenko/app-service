package com.kromanenko.appservice.service;

import com.kromanenko.appservice.model.Game;
import java.util.List;

public interface GameService {

  Game createGame(String gameName);

  boolean isGameExists(String gameName);

  Game findGameById(String id);

  Game findGameByName(String name);

  void deleteGame(String id);

  List<Game> findAllGames();
}
