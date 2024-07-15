package com.kromanenko.appservice.dto.dockercompose;

import com.kromanenko.appservice.util.ValidationPatterns;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DockerComposeConfigDto {

  @Min(value = 1024, message = "Port number must be greater than or equal to 1024")
  @Max(value = 65535, message = "Port number must be less than or equal to 65535")
  private int port;

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

  @NotBlank(message = "Volume is required")
  @Pattern(regexp = ValidationPatterns.VOLUME_PATTERN, message = "Volume path must be a valid Unix path")
  private String volume;
}