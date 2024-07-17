package com.kromanenko.appservice.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kromanenko.appservice.model.PostgreSqlDockerComposeConfig;
import org.junit.jupiter.api.Test;

class PostgreSqlDockerComposeGeneratorTest {

  @Test
  void shouldGenerateCorrectContent() throws Exception {
    // given
    PostgreSqlDockerComposeConfig config = PostgreSqlDockerComposeConfig
        .builder()
        .port("5432:5432")
        .username("user")
        .password("password")
        .database("db")
        .volume("volume")
        .build();

    // when
    String content = PostgreSqlDockerComposeGenerator.createDockerComposeContent(config);

    // then
    assertTrue(content.contains("ports:\n      - \"5432:5432\""));
    assertTrue(content.contains("volumes:\n      - volume"));
    assertTrue(content.contains("POSTGRES_USER: \"user\""));
    assertTrue(content.contains("POSTGRES_PASSWORD: \"password\""));
    assertTrue(content.contains("POSTGRES_DB: \"db\""));
  }
}
