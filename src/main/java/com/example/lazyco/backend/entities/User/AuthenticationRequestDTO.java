package com.example.lazyco.backend.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequestDTO {
  private String username;
  private String secretKey;
}
