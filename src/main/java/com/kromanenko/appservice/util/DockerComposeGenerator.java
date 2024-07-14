package com.kromanenko.appservice.util;

import com.kromanenko.appservice.model.DockerComposeGeneratorConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import org.springframework.util.ResourceUtils;

public class DockerComposeGenerator {
  private static final String TEMPLATE_PATH = "classpath:templates/docker-compose-template.yml";


  public static String createDockerComposeContent(DockerComposeGeneratorConfig config) throws IOException {
    File templateFile = ResourceUtils.getFile(TEMPLATE_PATH);
    String templateContent = new String(Files.readAllBytes(templateFile.toPath()));

    Map<String, String> attributes = Map.of(
        DockerComposeAttributes.PORT, String.valueOf(config.getPort()),
        DockerComposeAttributes.USERNAME, config.getUsername(),
        DockerComposeAttributes.PASSWORD, config.getPassword(),
        DockerComposeAttributes.VOLUME, config.getVolume()
    );

    for (Map.Entry<String, String> entry : attributes.entrySet()) {
      templateContent = templateContent.replace("${%s}".formatted(entry.getKey()), entry.getValue());
    }

    return templateContent;
  }
}
