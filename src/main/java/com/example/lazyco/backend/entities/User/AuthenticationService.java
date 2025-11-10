package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Exceptions.UnauthorizedException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;

  public AuthenticationService(@Lazy AuthenticationManager authenticationManager) {
    super();
    this.authenticationManager = authenticationManager;
  }

  public String loginAndGetToken(UserDTO userDTO) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  userDTO.getUsername(), userDTO.getPassword()));
      if (authentication.isAuthenticated()) {
        return JwtUtil.generateToken(userDTO.getUsername());
      } else {
        throw new UnauthorizedException(UserMessage.INCORRECT_PASSWORD);
      }
    } catch (BadCredentialsException e) {
      throw new UnauthorizedException(UserMessage.USER_NOT_FOUND);
    }
  }
}
