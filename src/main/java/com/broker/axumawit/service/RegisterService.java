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
import com.broker.axumawit.enums.GenderEnum;
import com.broker.axumawit.enums.RoleEnum;
import com.broker.axumawit.repository.UserRepository;

@Service
public class RegisterService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${app.upload.dir}")
  private String uploadDir;

  public Map<String, Object> registerUser(RegisterDto registerDto) {
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
    User savedUser = userRepository.save(user);
    // ## TODO might need to return a jwt for this
    return Map.of("result", "success", "message", "Successfully Registered!", "registeredUser", savedUser);
  }

  public User registerWithProfilePic(RegisterDto registerDto, MultipartFile file) {
    // might need to check before we save them
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
    String profilePicUrl = uploadProfilePic(file);
    user.setProfilePicUrl(profilePicUrl);
    System.out.println(("the profile pic url is " + profilePicUrl));
    User savedUser = userRepository.save(user);
    return savedUser;
  }

  public String uploadProfilePic(MultipartFile file) {
    // don't forget that this doesn't have the proper url
    String defaultProfilePic = "some_default_url_that_is_default_profile_pic";
    if (file.isEmpty()) {
      return defaultProfilePic;
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
      return defaultProfilePic;
    }
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
