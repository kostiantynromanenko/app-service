package com.kromanenko.appservice.service;

public interface StorageService {
  void createBucket(String bucketName);
  void uploadFile(String bucketName, String objectName, String fileName);
  void downloadFile(String bucketName, String objectName, String fileName);
  void deleteFile(String bucketName, String objectName);
}
