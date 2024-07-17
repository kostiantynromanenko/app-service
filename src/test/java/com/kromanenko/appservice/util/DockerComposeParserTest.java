package com.kromanenko.appservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kromanenko.appservice.dto.dockercompose.DockerComposeFileConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class DockerComposeParserTest {

  @Test
  void shouldReturnCorrectConfig() throws IOException {
    // given
    String yamlContent = """
        version: '3'
        services:
          web:
            image: "nginx:alpine"
        """;

    ByteArrayInputStream inputStream = new ByteArrayInputStream(yamlContent.getBytes());

    // when
    DockerComposeFileConfig config = DockerComposeParser.parseDockerComposeFile(inputStream);

    // then
    assertEquals("3", config.getVersion());
    assertNotNull(config.getServices());
    assertTrue(config.getServices().containsKey("web"));
    assertEquals("nginx:alpine", config.getServices().get("web").getImage());
  }
}
