package com.broker.axumawit.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.broker.axumawit.domain.User;
import com.broker.axumawit.dto.UserDto;
import com.broker.axumawit.repository.UserRepository;
import com.broker.axumawit.service.storage.StorageService;
import com.broker.axumawit.enums.GenderEnum;

@Service
public class UserService {

  @Autowired
  private AuthService authService;

  @Autowired
  private StorageService storageService;

  @Autowired
  private UserRepository userRepository;

  public User getProfile() {
    User user = authService.getAuthenticatedUser();
    if (user == null)
      return null; // if there is not authenticated user send null (for readabilty)
    user.setPassword(null);
    user.setProfilePicUrl(null);
    return user;
  }

  public User getUser(String username) {
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isPresent()) {
      return userOpt.get();
    }
    return null;
  }

  public byte[] getMyProfilePic() {
    User user = authService.getAuthenticatedUser();
    String filename = storageService.getNameFromUrl(user.getProfilePicUrl());
    return storageService.getImage(filename);
  }

  public UserDto convertToDto(User user) {
    return UserDto.builder()
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .username(user.getUsername())
        .email(user.getEmail())
        .gender(Optional.ofNullable(user.getGender()).map(GenderEnum::getGender).orElse(null))
        .role(user.getRole().name())
        .profilePicUrl(user.getProfilePicUrl())
        .build();
  }

}
