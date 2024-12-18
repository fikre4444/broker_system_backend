package com.broker.axumawit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.broker.axumawit.domain.User;
import com.broker.axumawit.dto.LoginDto;

@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private JwtService jwtService;

  public String authenticateAccount(LoginDto loginDto) {
    Authentication authentication = authManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
    // ##TODO create a proper handling for the failure case
    if (authentication.isAuthenticated())
      return jwtService.generateToken(loginDto.getUsername());
    return "failed Authentication";

  }

  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      System.out.println(authentication.getPrincipal());
      return (User) authentication.getPrincipal(); // Returns the username
    }
    return null;
  }

}
