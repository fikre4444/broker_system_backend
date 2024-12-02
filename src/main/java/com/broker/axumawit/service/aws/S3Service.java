package com.broker.axumawit.service.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class S3Service {

  @Autowired
  private S3Client s3Client;

  @Value("${aws.s3.bucket.name}")
  private String bucketName;

  // create-folder
  public void createFolder(String folderName) {
    // Ensure the folder name ends with a "/"
    if (!folderName.endsWith("/")) {
      folderName += "/";
    }

    // Create a "folder" by uploading an empty object with the folder name
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(folderName)
            .build(),
        software.amazon.awssdk.core.sync.RequestBody.empty());

    System.out.println("Folder created: " + folderName);
  }

  public void uploadMultipartFile(String key, MultipartFile file) throws IOException {
    // Upload the file to S3
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build(),
        RequestBody.fromBytes(file.getBytes()) // Convert MultipartFile to bytes
    );
    System.out.println("File uploaded successfully: " + key);

  }

  // Upload File from a file path
  public void uploadFileByPath(String key, String filePath) {
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build(),
        RequestBody.fromFile(Paths.get(filePath)));
  }

  public byte[] downloadFile(String key) throws RuntimeException {
    try {
      // Build the GetObject request
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();

      // Retrieve the file as bytes
      ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);

      return responseBytes.asByteArray(); // Return the file content as a byte array
    } catch (S3Exception e) {
      throw new RuntimeException("Failed to download file from S3", e);
    }
  }

  // Download File to some path
  public void downloadFile(String bucketName, String key, String destinationPath) {
    s3Client.getObject(
        GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build(),
        Paths.get(destinationPath));
  }

  // List Buckets
  public void listBuckets() {
    ListBucketsResponse bucketsResponse = s3Client.listBuckets();
    bucketsResponse.buckets().forEach(bucket -> System.out.println(bucket.name()));
  }

  // List Objects in a Bucket
  public void listObjects(String bucketName) {
    ListObjectsV2Response objectsResponse = s3Client.listObjectsV2(
        ListObjectsV2Request.builder().bucket(bucketName).build());
    objectsResponse.contents().forEach(object -> System.out.println(object.key()));
  }
}
