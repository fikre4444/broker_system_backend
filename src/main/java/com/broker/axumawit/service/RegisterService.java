package com.broker.axumawit.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.broker.axumawit.domain.User;
import com.broker.axumawit.dto.RegisterDto;
import com.broker.axumawit.dto.UserDto;
import com.broker.axumawit.enums.GenderEnum;
import com.broker.axumawit.enums.RoleEnum;
import com.broker.axumawit.repository.UserRepository;
import com.broker.axumawit.service.storage.StorageService;

@Service
public class RegisterService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  StorageService storageService;

  @Autowired
  private UserService userService;

  public Map<String, Object> registerUser(RegisterDto registerDto) {
    // register through normal json object
    User user = User.builder()
        .firstName(registerDto.getFirstName())
        .lastName(registerDto.getLastName())
        .username(registerDto.getUsername())
        .email(registerDto.getEmail())
        .gender(registerDto.getGender())
        .password(passwordEncoder.encode(registerDto.getPassword()))
        .role(RoleEnum.ROLE_USER)
        .created_at(LocalDateTime.now())
        .build();
    String profilePicUrl = storageService.getDefaultProfilePicUrl();
    user.setProfilePicUrl(profilePicUrl);
    User savedUser = userRepository.save(user);
    // ## TODO might need to return a jwt for this
    return Map.of("result", "success", "message", "Successfully Registered!", "registeredUser", savedUser);
  }

  public UserDto registerWithProfilePic(RegisterDto registerDto, MultipartFile file) {
    // register through form data and image
    // ## TODO check for the uniqueness of either email or username not sure yet
    User user = User.builder()
        .firstName(registerDto.getFirstName())
        .lastName(registerDto.getLastName())
        .username(registerDto.getUsername())
        .email(registerDto.getEmail())
        .gender(registerDto.getGender())
        .password(passwordEncoder.encode(registerDto.getPassword()))
        .role(RoleEnum.ROLE_USER)
        .created_at(LocalDateTime.now())
        .build();
    String profilePicUrl = storageService.uploadProfilePic(file);
    user.setProfilePicUrl(profilePicUrl);
    System.out.println(("the profile pic url is " + profilePicUrl));
    User savedUser = userRepository.save(user);
    return userService.convertToDto(savedUser);
  }

  // Just temporary need to delete later
  public Map<String, Object> registerAdmin() {
    User user = User.builder()
        .firstName("Jacob")
        .lastName("Issiah")
        .username("jacob11")
        .email("jacob2@gmail.com")
        .gender(GenderEnum.MALE)
        .password(passwordEncoder.encode("1234"))
        .role(RoleEnum.ROLE_ADMINISTRATOR)
        .created_at(LocalDateTime.now())
        .build();
    User savedUser = userRepository.save(user);
    return Map.of("result", "success", "message", "Successfully Registered!", "registeredUser", savedUser);
  }

}
