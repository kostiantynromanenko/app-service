package com.kromanenko.appservice.repository;

import com.kromanenko.appservice.model.Game;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {

  Optional<Game> findByName(String name);
}
