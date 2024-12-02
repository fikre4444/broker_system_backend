package com.broker.axumawit.service.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "local")
public class LocalStorageService implements StorageService {

  @Value("${app.upload.dir}")
  private String uploadDir;

  public String getDefaultProfilePicUrl() {
    // gets the default profile pic url for a user that didn't submit a profile pic
    try {
      // Create upload directory if it doesn't exist
      Path dirPath = Paths.get(uploadDir);
      if (!Files.exists(dirPath)) {
        Files.createDirectories(dirPath);
      }

      // Save the file
      String fileName = "default.jpg";
      Path filePath = dirPath.resolve(fileName);
      return filePath.toString();
    } catch (IOException ex) {
      System.out.println("something happened lol");
      return null;
    }
  }

  @Override
  public String uploadProfilePic(MultipartFile file) {
    // uploads profile pic or sets the default if an error occurs
    if (file == null || file.isEmpty()) {
      return getDefaultProfilePicUrl();
    }

    try {
      // Create upload directory if it doesn't exist
      Path dirPath = Paths.get(uploadDir);
      if (!Files.exists(dirPath)) {
        Files.createDirectories(dirPath);
      }

      // Save the file
      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
      Path filePath = dirPath.resolve(fileName);
      file.transferTo(filePath.toFile());

      return filePath.toString();
    } catch (IOException ex) {
      ex.printStackTrace();
      System.out.println("An error occured while uploading the image changing to default profilepic");
      return getDefaultProfilePicUrl();
    }
  }

  @Override
  public byte[] getImage(String fileName) {
    try {
      Path imagePath = Paths.get(uploadDir).resolve(fileName);
      byte[] imageBytes = Files.readAllBytes(imagePath);
      return imageBytes;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Thre was a problem while fetching that thing iamge");
      return null;
    }
  }

}
