package com.kromanenko.appservice.controller;

import com.kromanenko.appservice.dto.game.CreateGameRequest;
import com.kromanenko.appservice.dto.game.GameResponse;
import com.kromanenko.appservice.facade.GameFacade;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

  private final GameFacade gameFacade;

  @PostMapping
  public GameResponse createGame(@Valid @RequestBody CreateGameRequest request) {
    return gameFacade.createGame(request);
  }

  @GetMapping
  public List<GameResponse> getAllGames() {
    return gameFacade.getAllGames();
  }
}
