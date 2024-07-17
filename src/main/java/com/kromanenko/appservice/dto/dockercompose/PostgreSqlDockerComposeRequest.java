package com.kromanenko.appservice.dto.dockercompose;

import com.kromanenko.appservice.util.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostgreSqlDockerComposeRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 3, message = "Username must be at least 3 characters long")
  @Pattern(regexp = ValidationPatterns.USERNAME_PATTERN,
      message = "Username name must contain only letters, digits, and underscores")
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(regexp = ValidationPatterns.PASSWORD_PATTERN,
      message = "Password must contain at least one digit, one letter, and one special character (@#$%^&+=)")
  private String password;

  @NotBlank(message = "Database name is required")
  @Size(min = 3, message = "Database name must be at least 3 characters long")
  @Pattern(regexp = ValidationPatterns.DATABASE_NAME_PATTERN,
      message = "Database name must contain only letters, digits, and underscores")
  private String database;

  @NotBlank(message = "Volume is required")
  @Pattern(regexp = ValidationPatterns.POSTGRESQL_VOLUME_PATTERN, message = "Invalid volume format. Expected format: /path:/var/lib/postgresql/data, where /path is a valid directory path")
  private String volume;

  @NotBlank(message = "Port mapping is required")
  @Pattern(regexp = ValidationPatterns.PORT_PATTERN, message = "Port mapping must be in the format host:container. Port numbers must be between 1025 and 65535")
  private String port;
}
