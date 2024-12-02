package com.broker.axumawit.service.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.broker.axumawit.service.aws.S3Service;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
public class S3StorageService implements StorageService {

  @Value("${aws.upload.dir}")
  private String uploadDir;

  @Value("${aws.default.profile.pic}")
  private String defaultProfilePicture;

  @Autowired
  private S3Service s3Service;

  @Override
  public String getDefaultProfilePicUrl() {
    return defaultProfilePicture;
  }

  @Override
  public String uploadProfilePic(MultipartFile file) {
    // uploads profile pic or sets the default if an error occurs
    if (file == null || file.isEmpty()) {
      return getDefaultProfilePicUrl();
    }

    try {
      String filePath = uploadDir;
      // Save the file
      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
      // this is the total uri of the pic
      String key = filePath + fileName;
      s3Service.uploadMultipartFile(key, file);
      return key;
    } catch (IOException ex) {
      ex.printStackTrace();
      System.out.println("An error occured while uploading the image changing to default profilepic");
      return getDefaultProfilePicUrl();
    }

  }

  @Override
  public byte[] getImage(String fileName) {
    try {
      String photoUri = uploadDir + fileName;
      byte[] imageBytes = s3Service.downloadFile(photoUri);
      return imageBytes;
    } catch (RuntimeException ex) {
      ex.printStackTrace();
      System.out.println("there was a problem while fetching image!");
      return null;
    }
  }

  @Override
  public String getNameFromUrl(String url) {
    int lastIndexOfSlash = url.lastIndexOf("/");
    return url.substring(lastIndexOfSlash + 1);
  }

}
