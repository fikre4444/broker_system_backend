package com.broker.axumawit.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.broker.axumawit.domain.User;
import com.broker.axumawit.dto.RegisterDto;
import com.broker.axumawit.enums.GenderEnum;
import com.broker.axumawit.enums.RoleEnum;
import com.broker.axumawit.repository.UserRepository;
import com.broker.axumawit.service.RegisterService;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/register/")
public class RegisterController {

  @Autowired
  private RegisterService registerService;

  // might add profile pic adding later
  @PostMapping("/registerUser")
  public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
    return ResponseEntity.ok(registerService.registerUser(registerDto));
  }

  @GetMapping("/hello")
  public String sayHello() {
    return "hello from register";
  }

  @PostMapping("/registerAdmin")
  public ResponseEntity<?> registerAdmin() {
    return ResponseEntity.ok(registerService.registerAdmin());
  }

  @Value("${app.upload.dir}")
  private String uploadDir;

  @Autowired
  private UserRepository userProfileRepository;

  @PostMapping("/register-with-profile-pic")
  public ResponseEntity<?> registerWithProfilePic(@RequestPart RegisterDto registerDto,
      @RequestPart MultipartFile file) {
    try {
      User user = registerService.registerWithProfilePic(registerDto, file);
      // ##TODO might fix later
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  // @PostMapping("/upload")
  // public ResponseEntity<String> uploadProfile(@RequestPart RegisterDto
  // registerDto,
  // @RequestPart MultipartFile file) {

  // System.out.println("the gotten dto is " + registerDto);
  // // Validate file
  // if (file.isEmpty()) {
  // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image file is
  // required");
  // }

  // try {
  // // Create upload directory if it doesn't exist
  // Path dirPath = Paths.get(uploadDir);
  // if (!Files.exists(dirPath)) {
  // Files.createDirectories(dirPath);
  // }

  // // Save the file
  // String fileName = System.currentTimeMillis() + "_" +
  // file.getOriginalFilename();
  // Path filePath = dirPath.resolve(fileName);
  // file.transferTo(filePath.toFile());

  // User user = User.builder()
  // .firstName("Jacob")
  // .lastName("Issiah")
  // .profilePicUrl(filePath.toString())
  // .username("jacob11")
  // .email("jacob2@gmail.com")
  // .gender(GenderEnum.MALE)
  // .password("ababye")
  // .role(RoleEnum.ROLE_ADMINISTRATOR)
  // .created_at(LocalDateTime.now())
  // .build();
  // userProfileRepository.save(user);

  // return ResponseEntity.ok("Image uploaded successfully: " + fileName);

  // } catch (IOException e) {
  // e.printStackTrace();
  // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed
  // to upload image");
  // }
  // }

  @GetMapping("/image/{filename}")
  public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
    try {
      Path imagePath = Paths.get(uploadDir).resolve(filename);
      byte[] imageBytes = Files.readAllBytes(imagePath);
      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

}
