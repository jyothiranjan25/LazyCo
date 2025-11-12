package com.example.lazyco.backend.entities.User;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

  private static final Argon2PasswordEncoder argon2 =
      new Argon2PasswordEncoder(16, 32, 1, 65536, 3);

  @Override
  public String encode(CharSequence rawPassword) {
    return argon2.encode(rawPassword);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return argon2.matches(rawPassword, encodedPassword);
  }
}
