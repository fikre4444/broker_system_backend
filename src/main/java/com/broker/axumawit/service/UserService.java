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
import com.broker.axumawit.service.storage.LocalStorageService;

@Service
public class UserService {

  @Autowired
  private AuthService authService;

  @Autowired
  private LocalStorageService localStorageService;

  public User getProfile() {
    User user = authService.getAuthenticatedUser();
    if (user == null)
      return null; // if there is not authenticated user send null (for readabilty)
    user.setPassword(null);
    user.setProfilePicUrl(null);
    return user;
  }

  public byte[] getMyProfilePic() {
    User user = authService.getAuthenticatedUser();
    int lastIndexOfSlash = user.getProfilePicUrl().lastIndexOf("\\");
    String filename = user.getProfilePicUrl().substring(lastIndexOfSlash + 1);
    return localStorageService.getImage(filename);
  }

}
