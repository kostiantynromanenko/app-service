package com.kromanenko.appservice.exception.handler;

import com.kromanenko.appservice.dto.ErrorResponse;
import com.kromanenko.appservice.exception.BucketAlreadyExistsException;
import com.kromanenko.appservice.exception.DockerComposeFileNotFound;
import com.kromanenko.appservice.exception.GameAlreadyExistsException;
import com.kromanenko.appservice.exception.GameNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ex.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(GameAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleGameAlreadyExistsException(
      GameAlreadyExistsException ex) {
    var errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(),
        System.currentTimeMillis());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(BucketAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleBucketAlreadyExistsException(
      BucketAlreadyExistsException ex) {
    var errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(),
        System.currentTimeMillis());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DockerComposeFileNotFound.class)
  public ResponseEntity<ErrorResponse> handleDockerComposeFileNotFound(
      DockerComposeFileNotFound ex) {
    var errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
        System.currentTimeMillis());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(GameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleGameNotFoundException(GameNotFoundException ex) {
    var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
        System.currentTimeMillis());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
