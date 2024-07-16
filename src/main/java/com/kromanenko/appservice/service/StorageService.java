package com.kromanenko.appservice.service;

import java.io.InputStream;

public interface StorageService {

  void createBucket(String bucketName);

  void uploadFile(String bucketName, String objectName, InputStream inputStream,
      String contentType);

  boolean fileExists(String bucketName, String objectName);

  boolean bucketExists(String bucketName);

  InputStream getFileInputStream(String bucketName, String objectName);

  void deleteFile(String bucketName, String objectName);
}
