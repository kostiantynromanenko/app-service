package com.kromanenko.appservice.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kromanenko.appservice.dto.dockercompose.DockerComposeFileConfig;
import com.kromanenko.appservice.exception.BucketNotFoundException;
import com.kromanenko.appservice.exception.DockerComposeFileNotFound;
import com.kromanenko.appservice.exception.DockerComposeServiceException;
import com.kromanenko.appservice.service.impl.DockerComposeService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DockerComposeServiceTest {

  @Mock
  private StorageService storageService;

  @InjectMocks
  private DockerComposeService dockerComposeService;

  @Test
  void shouldGetDockerComposeFileInputStreamWhenFileExists() {
    // given
    when(storageService.bucketExists(anyString())).thenReturn(true);
    when(storageService.fileExists(anyString(), anyString())).thenReturn(true);
    when(storageService.getFileInputStream(anyString(), anyString())).thenReturn(
        new ByteArrayInputStream(new byte[0]));

    // when
    InputStream result = dockerComposeService.getDockerComposeFileInputStream("testBucket");

    // then
    Assertions.assertNotNull(result);
    verify(storageService, times(1)).getFileInputStream("testBucket", "docker-compose.yml");
  }

  @Test
  void shouldThrowBucketNotFoundExceptionWhenBucketDoesNotExistOnGetInputStream() {
    // given
    when(storageService.bucketExists(anyString())).thenReturn(false);

    // then
    assertThrows(BucketNotFoundException.class,
        () -> dockerComposeService.getDockerComposeFileInputStream("testBucket"));
  }

  @Test
  void shouldThrowDockerComposeFileNotFoundWhenFileDoesNotExist() {
    when(storageService.bucketExists(anyString())).thenReturn(true);
    when(storageService.fileExists(anyString(), anyString())).thenReturn(false);

    assertThrows(DockerComposeFileNotFound.class,
        () -> dockerComposeService.getDockerComposeFileInputStream("testBucket"));
  }

  @Test
  void shouldThrowDockerComposeServiceExceptionOnGetInputStreamFailure() {
    // given
    when(storageService.bucketExists(anyString())).thenReturn(true);
    when(storageService.fileExists(anyString(), anyString())).thenReturn(true);
    doThrow(RuntimeException.class).when(storageService)
        .getFileInputStream(anyString(), anyString());

    // then
    assertThrows(DockerComposeServiceException.class,
        () -> dockerComposeService.getDockerComposeFileInputStream("testBucket"));
  }

  @Test
  void shouldGetDockerComposeFileConfigSuccessfully() {
    // given
    when(storageService.bucketExists(anyString())).thenReturn(true);
    when(storageService.fileExists(anyString(), anyString())).thenReturn(true);
    when(storageService.getFileInputStream(anyString(), anyString())).thenReturn(
        new ByteArrayInputStream("version: '3'".getBytes()));

    // when
    DockerComposeFileConfig config = dockerComposeService.getDockerComposeFileConfig("testBucket");

    // then
    Assertions.assertNotNull(config);
  }

  @Test
  void shouldThrowDockerComposeServiceExceptionOnParseFailure() {
    // given
    when(storageService.bucketExists(anyString())).thenReturn(true);
    when(storageService.fileExists(anyString(), anyString())).thenReturn(true);
    when(storageService.getFileInputStream(anyString(), anyString())).thenReturn(
        new ByteArrayInputStream(new byte[0]));

    // then
    assertThrows(DockerComposeServiceException.class,
        () -> dockerComposeService.getDockerComposeFileConfig("testBucket"));
  }

  @Test
  void shouldDeleteDockerComposeFileSuccessfully() {
    // given
    when(storageService.bucketExists(anyString())).thenReturn(true);

    // when
    dockerComposeService.deleteDockerComposeFile("testBucket");

    // then
    verify(storageService, times(1)).deleteFile("testBucket", "docker-compose.yml");
  }

  @Test
  void shouldThrowBucketNotFoundExceptionWhenBucketDoesNotExistOnDelete() {
    // given
    when(storageService.bucketExists(anyString())).thenReturn(false);

    // then
    assertThrows(BucketNotFoundException.class,
        () -> dockerComposeService.deleteDockerComposeFile("testBucket"));
  }

  @Test
  void shouldThrowDockerComposeServiceExceptionOnDeleteFailure() {
    // given
    when(storageService.bucketExists(anyString())).thenReturn(true);
    doThrow(RuntimeException.class).when(storageService).deleteFile(anyString(), anyString());

    // then
    assertThrows(DockerComposeServiceException.class,
        () -> dockerComposeService.deleteDockerComposeFile("testBucket"));
  }
}
