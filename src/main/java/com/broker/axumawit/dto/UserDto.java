package com.broker.axumawit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
  private String firstName, lastName, username, email;
  private String profilePicUrl; // not needed, just in case
  private String gender;
  private String role;
}
