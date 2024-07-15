package com.kromanenko.appservice.util;

import com.kromanenko.appservice.model.DockerComposeConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class DockerComposeGenerator {

  private static final String TEMPLATE_PATH = "templates/docker-compose-template.yaml";

  public static String createDockerComposeContent(DockerComposeConfig config)
      throws IOException {
    Resource resource = new ClassPathResource(TEMPLATE_PATH);
    String templateContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));

    Map<String, String> params = Map.of(
        DockerComposeParams.PORT, String.valueOf(config.getPort()),
        DockerComposeParams.USERNAME, config.getUsername(),
        DockerComposeParams.PASSWORD, config.getPassword(),
        DockerComposeParams.VOLUME, config.getVolume()
    );

    for (Map.Entry<String, String> entry : params.entrySet()) {
      templateContent = templateContent.replace("${%s}".formatted(entry.getKey()),
          entry.getValue());
    }

    return templateContent;
  }
}
