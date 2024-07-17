package com.kromanenko.appservice.service.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.kromanenko.appservice.exception.StorageServiceException;
import com.kromanenko.appservice.service.impl.MinioStorageService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
public class MinioStorageServiceIntegrationTest {

  @Container
  static MinIOContainer minioContainer = new MinIOContainer(
      "minio/minio:RELEASE.2023-09-04T19-57-37Z")
      .withEnv("MINIO_ACCESS_KEY", "minioadmin")
      .withEnv("MINIO_SECRET_KEY", "minioadmin")
      .withExposedPorts(9000)
      .withCommand("server", "/data");

  @DynamicPropertySource
  static void minioProperties(DynamicPropertyRegistry registry) {
    registry.add("minio.endpoint", () -> String.format("http://%s:%s", minioContainer.getHost(),
        minioContainer.getMappedPort(9000)));
    registry.add("minio.access-key", () -> "minioadmin");
    registry.add("minio.secret-key", () -> "minioadmin");
  }

  @Autowired
  private MinioStorageService minioStorageService;

  @BeforeAll
  static void setup() {
    minioContainer.start();
  }

  @AfterAll
  static void tearDown() {
    minioContainer.stop();
  }

  @Test
  void shouldCreateBucket() {
    // given
    var bucketName = "test-bucket-1";
    minioStorageService.createBucket(bucketName);

    // then
    Assertions.assertTrue(minioStorageService.bucketExists(bucketName));
  }

  @Test
  void shouldThrowExceptionIfBucketAlreadyExists() {
    // given
    var bucketName = "test-bucket-2";
    minioStorageService.createBucket(bucketName);

    // when
    assertThrows(StorageServiceException.class,
        () -> minioStorageService.createBucket(bucketName));
  }

  @Test
  void shouldUploadFile() throws Exception {
    // given
    var bucketName = "test-bucket-3";
    var objectName = "test-object-3";

    minioStorageService.createBucket(bucketName);

    // when
    updateTestFile(bucketName, objectName);

    // then
    Assertions.assertTrue(minioStorageService.fileExists(bucketName, objectName));
  }

  @Test
  void shouldReturnFalseIfFileNotExists() {
    // given
    var bucketName = "test-bucket-3";
    var objectName = "test-object-4";

    // when
    var result = minioStorageService.fileExists(bucketName, objectName);

    // then
    Assertions.assertFalse(result);
  }

  @Test
  void shouldReturnTrueIfFileExists() throws Exception {
    // given
    var bucketName = "test-bucket-5";
    var objectName = "test-object-5";

    minioStorageService.createBucket(bucketName);
    updateTestFile(bucketName, objectName);

    // when
    var result = minioStorageService.fileExists(bucketName, objectName);

    // then
    Assertions.assertTrue(result);
  }

  @Test
  void shouldDeleteFile() throws Exception {
    // given
    var bucketName = "test-bucket-6";
    var objectName = "test-object-6";

    minioStorageService.createBucket(bucketName);
    updateTestFile(bucketName, objectName);

    // then
    Assertions.assertTrue(minioStorageService.fileExists(bucketName, objectName));

    // when
    minioStorageService.deleteFile(bucketName, objectName);

    // then
    Assertions.assertFalse(minioStorageService.fileExists(bucketName, objectName));
  }

  @Test
  void shouldVerifyUploadedFileContent() throws Exception {
    // given
    var bucketName = "test-bucket-verify-content";
    var objectName = "test-object-verify-content";
    var expectedContent = "test data";

    minioStorageService.createBucket(bucketName);
    updateTestFile(bucketName, objectName);

    // when
    var downloadedStream = minioStorageService.getFileInputStream(bucketName, objectName);
    var downloadedContent = new String(downloadedStream.readAllBytes(), StandardCharsets.UTF_8);

    // then
    Assertions.assertEquals(expectedContent, downloadedContent);
  }

  private void updateTestFile(String bucketName, String fileName) throws Exception {
    var file = new MockMultipartFile("file", fileName,
        "text/plain", "test data".getBytes());

    minioStorageService.uploadFile(bucketName, fileName, file.getInputStream(), "text/plain");
  }
}
