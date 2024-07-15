package com.kromanenko.appservice.service;

import com.kromanenko.appservice.model.Game;
import java.util.List;

public interface GameService {

  Game createGame(String gameName);

  boolean gameExistsByName(String gameName);

  boolean gameExistsById(String gameId);

  List<Game> findAllGames();
}
