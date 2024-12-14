package com.broker.axumawit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.broker.axumawit.domain.User;
import com.broker.axumawit.dto.RegisterDto;
import com.broker.axumawit.dto.UserDto;
import com.broker.axumawit.service.JwtService;
import com.broker.axumawit.service.RegisterService;

@RestController
@RequestMapping("/api/register/")
public class RegisterController {

  @Autowired
  private RegisterService registerService;

  @Autowired
  private JwtService jwtService;

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

  @PostMapping("/register-with-profile-pic")
  public ResponseEntity<?> registerWithProfilePic(@RequestPart RegisterDto registerDto,
      @RequestPart(name = "file", value = "file", required = false) MultipartFile file) {
    try {
      UserDto userDto = registerService.registerWithProfilePic(registerDto, file);
      String token = jwtService.generateToken(userDto.getUsername());
      return ResponseEntity.ok()
          .header(HttpHeaders.AUTHORIZATION, token)
          .body(userDto);
    } catch (Exception e) {
      System.out.println("some shit happened");
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
