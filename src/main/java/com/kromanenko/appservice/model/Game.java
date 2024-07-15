package com.kromanenko.appservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "games")
public class Game {

  private String id;
  private String name;
}
