package com.broker.axumawit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broker.axumawit.domain.User;
import com.broker.axumawit.dto.LoginDto;
import com.broker.axumawit.dto.UserDto;
import com.broker.axumawit.service.AuthService;
import com.broker.axumawit.service.RegisterService;
import com.broker.axumawit.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    String token = authService.authenticateAccount(loginDto);
    if (token.equals("failed Authentication")) {
      return ResponseEntity.badRequest().build();
    }
    User user = userService.getUser(loginDto.getUsername());
    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, token)
        .body(userService.convertToDto(user));
  }

  @GetMapping("/hello")
  public String sayHello() {
    return "hello from auth";
  }

}
