package com.kromanenko.appservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateDockerComposeRequest extends DockerComposeRequest {}
