package com.kromanenko.appservice.service;

import java.io.InputStream;

public interface StorageService {

  void createBucket(String bucketName);

  void uploadFile(String bucketName, String objectName, InputStream inputStream,
      String contentType);

  void downloadFile(String bucketName, String objectName, String fileName);

  void deleteFile(String bucketName, String objectName);
}
