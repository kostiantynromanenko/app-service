package com.kromanenko.appservice.service.impl;

import com.kromanenko.appservice.exception.StorageServiceException;
import com.kromanenko.appservice.service.StorageService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

  private static final long UPLOAD_STREAM_PART_SIZE = -1;
  private static final String NO_SUCH_KEY = "NoSuchKey";

  private final MinioClient minioClient;

  @Override
  public void createBucket(String bucketName) {
    var args = MakeBucketArgs.builder()
        .bucket(bucketName)
        .build();

    try {
      minioClient.makeBucket(args);
    } catch (Exception e) {
      throw new StorageServiceException("Failed to create bucket", e);
    }
  }

  @Override
  public void uploadFile(String bucketName, String objectName, InputStream inputStream,
      String contentType) {
    try {
      var args = PutObjectArgs.builder()
          .bucket(bucketName)
          .object(objectName)
          .stream(inputStream, inputStream.available(),
              UPLOAD_STREAM_PART_SIZE)
          .contentType(contentType)
          .build();

      minioClient.putObject(args);
    } catch (Exception e) {
      throw new StorageServiceException("Failed to upload file", e);
    }
  }

  @Override
  public boolean fileExists(String bucketName, String objectName) {
    var args = StatObjectArgs.builder()
        .bucket(bucketName)
        .object(objectName)
        .build();
    try {
      minioClient.statObject(args);
      return true;
    } catch (ErrorResponseException e) {
      if (e.errorResponse().code().equals(NO_SUCH_KEY)) {
        return false;
      } else {
        throw new StorageServiceException("Error checking if object exists", e);
      }
    } catch (Exception e) {
      throw new StorageServiceException("Error checking if object exists", e);
    }
  }

  @Override
  public boolean bucketExists(String bucketName) {
    var args = BucketExistsArgs.builder()
        .bucket(bucketName)
        .build();
    try {
      return minioClient.bucketExists(args);
    } catch (Exception e) {
      throw new StorageServiceException("Error checking if bucket exists", e);
    }
  }

  @Override
  public InputStream getFileInputStream(String bucketName, String objectName) {
    var args = GetObjectArgs.builder()
        .bucket(bucketName)
        .object(objectName)
        .build();

    try {
      return minioClient.getObject(args);
    } catch (Exception e) {
      throw new StorageServiceException("Failed to get file", e);
    }
  }

  @Override
  public void deleteFile(String bucketName, String objectName) {
    var args = RemoveObjectArgs.builder()
        .bucket(bucketName)
        .object(objectName)
        .build();

    try {
      minioClient.removeObject(args);
    } catch (Exception e) {
      throw new StorageServiceException("Failed to delete file", e);
    }
  }
}
