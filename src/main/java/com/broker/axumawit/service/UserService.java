package com.broker.axumawit.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.broker.axumawit.domain.User;

@Service
public class UserService {

  @Value("${app.upload.dir}")
  private String uploadDir;

  @Autowired
  private AuthService authService;

  public User getProfile() {
    User user = authService.getAuthenticatedUser();
    if (user == null)
      return null; // if there is not authenticated user send null (for readabilty)
    user.setPassword(null);
    user.setProfilePicUrl(null);
    return user;
  }

  public byte[] getImage(String filename) {
    try {
      Path imagePath = Paths.get(uploadDir).resolve(filename);
      byte[] imageBytes = Files.readAllBytes(imagePath);
      return imageBytes;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Thre was a problem while fetching that thing iamge");
      return null;
    }
  }

  public byte[] getMyProfilePic() {
    User user = authService.getAuthenticatedUser();
    int lastIndexOfSlash = user.getProfilePicUrl().lastIndexOf("\\");
    String filename = user.getProfilePicUrl().substring(lastIndexOfSlash + 1);
    try {
      Path imagePath = Paths.get(uploadDir).resolve(filename);
      byte[] imageBytes = Files.readAllBytes(imagePath);
      return imageBytes;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Thre was a problem while fetching that thing iamge");
      return null;
    }
  }

}
