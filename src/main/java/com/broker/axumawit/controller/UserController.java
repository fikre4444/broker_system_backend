package com.broker.axumawit.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broker.axumawit.domain.User;
import com.broker.axumawit.service.UserService;

@RestController
@RequestMapping("/api/user/")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/say-hello")
  public String sayHello() {
    return "Hello User";
  }

  @GetMapping("/get-profile")
  public ResponseEntity<?> getProfile() {
    User user = userService.getProfile();
    if (user == null)
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    return ResponseEntity.ok(user);
  }

  @GetMapping("/image/{filename}")
  public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
    // byte[] result = userService.getImage(filename);
    byte[] result = userService.getMyProfilePic();
    if (result == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(result);
  }

  @GetMapping("/get-my-profile-pic")
  public ResponseEntity<byte[]> getMyProfilePic() {
    // byte[] result = userService.getImage(filename);
    byte[] result = userService.getMyProfilePic();
    if (result == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(result);
  }

}
