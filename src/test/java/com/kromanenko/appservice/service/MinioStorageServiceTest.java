package com.kromanenko.appservice.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kromanenko.appservice.exception.StorageServiceException;
import com.kromanenko.appservice.service.impl.MinioStorageService;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MinioStorageServiceTest {

  @Mock
  private MinioClient minioClient;

  @InjectMocks
  private MinioStorageService minioStorageService;


  @Test
  void shouldUploadFileSuccessfully() throws Exception {
    // given
    String bucketName = "test-bucket";
    String objectName = "test-object";
    InputStream inputStream = new ByteArrayInputStream(new byte[10]);

    // when
    minioStorageService.uploadFile(bucketName, objectName, inputStream, "application/octet-stream");

    // then
    verify(minioClient, times(1)).putObject(any());
  }

  @Test
  void shouldThrowStorageServiceExceptionWhenUploadFileFails() throws Exception {
    // given
    String bucketName = "test-bucket";
    String objectName = "test-object";
    InputStream inputStream = new ByteArrayInputStream(new byte[10]);
    doThrow(new RuntimeException()).when(minioClient).putObject(any());

    // then
    assertThrows(StorageServiceException.class,
        () -> minioStorageService.uploadFile(bucketName, objectName, inputStream,
            "application/octet-stream"));
  }

  @Test
  void shouldReturnTrueWhenBucketExists() throws Exception {
    // given
    String bucketName = "test-bucket";
    when(minioClient.bucketExists(any())).thenReturn(true);

    // then
    assertTrue(minioStorageService.bucketExists(bucketName));
  }

  @Test
  void shouldReturnFalseWhenBucketDoesNotExist() throws Exception {
    // given
    String bucketName = "test-bucket";
    when(minioClient.bucketExists(any())).thenReturn(false);

    // then
    assertFalse(minioStorageService.bucketExists(bucketName));
  }

  @Test
  void shouldThrowStorageServiceExceptionWhenCheckingBucketExistsFails() throws Exception {
    // given
    String bucketName = "test-bucket";
    doThrow(new RuntimeException()).when(minioClient).bucketExists(any());

    // then
    assertThrows(StorageServiceException.class, () -> minioStorageService.bucketExists(bucketName));
  }

  @Test
  void shouldGetFileInputStreamSuccessfully() throws Exception {
    // given
    String bucketName = "test-bucket";
    String objectName = "test-object";
    GetObjectResponse mockResponse = mock(GetObjectResponse.class);
    when(minioClient.getObject(any())).thenReturn(mockResponse);

    // when
    InputStream result = minioStorageService.getFileInputStream(bucketName, objectName);

    // then
    assertNotNull(result);
  }

  @Test
  void shouldThrowStorageServiceExceptionWhenGetFileInputStreamFails() throws Exception {
    // given
    String bucketName = "test-bucket";
    String objectName = "test-object";
    doThrow(new RuntimeException()).when(minioClient).getObject(any());

    // then
    assertThrows(StorageServiceException.class,
        () -> minioStorageService.getFileInputStream(bucketName, objectName));
  }

  @Test
  void shouldDeleteFileSuccessfully() throws Exception {
    // given
    String bucketName = "test-bucket";
    String objectName = "test-object";

    // when
    minioStorageService.deleteFile(bucketName, objectName);

    // then
    verify(minioClient, times(1)).removeObject(any());
  }

  @Test
  void shouldThrowStorageServiceExceptionWhenDeleteFileFails() throws Exception {
    // given
    String bucketName = "test-bucket";
    String objectName = "test-object";
    doThrow(new RuntimeException()).when(minioClient).removeObject(any());

    // then
    assertThrows(StorageServiceException.class,
        () -> minioStorageService.deleteFile(bucketName, objectName));
  }
}