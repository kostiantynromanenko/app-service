package com.kromanenko.appservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.kromanenko.appservice.dto.dockercompose.DockerComposeFileConfig;
import java.io.IOException;
import java.io.InputStream;

public class DockerComposeParser {

  public static DockerComposeFileConfig parseDockerComposeFile(InputStream inputStream) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    return mapper.readValue(inputStream, DockerComposeFileConfig.class);
  }
}
