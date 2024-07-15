package com.kromanenko.appservice.dto.game;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGameRequest {

  @NotBlank(message = "Name is required")
  private String name;
}
