package com.kromanenko.appservice.service.impl;

import com.kromanenko.appservice.exception.StorageServiceException;
import com.kromanenko.appservice.service.StorageService;
import io.minio.DownloadObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("minio")
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

  private final MinioClient minioClient;

  @Override
  public void createBucket(String bucketName) {
    var args = MakeBucketArgs.builder().bucket(bucketName).build();

    try {
      minioClient.makeBucket(args);
    } catch (Exception e) {
      throw new StorageServiceException("Failed to create bucket", e);
    }
  }

  @Override
  public void uploadFile(String bucketName, String objectName, String fileName) {
    try {
      var args = UploadObjectArgs.builder()
          .bucket(bucketName)
          .object(objectName)
          .filename(fileName)
          .build();

      minioClient.uploadObject(args);
    } catch (Exception e) {
      throw new StorageServiceException("Failed to upload file", e);
    }
  }

  @Override
  public void downloadFile(String bucketName, String objectName, String fileName) {
    var args = DownloadObjectArgs.builder()
        .bucket(bucketName)
        .object(objectName)
        .filename(fileName)
        .build();

    try {
      minioClient.downloadObject(args);
    } catch (Exception e) {
      throw new StorageServiceException("Failed to download file", e);
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
