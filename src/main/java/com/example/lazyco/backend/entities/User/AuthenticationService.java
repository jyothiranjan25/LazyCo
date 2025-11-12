package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Exceptions.ApplicationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  public String loginAndGetToken(UserDTO userDTO) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  userDTO.getUsername(), userDTO.getPassword()));
      if (authentication.isAuthenticated()) {
        return jwtUtil.generateToken(userDTO.getUsername());
      } else {
        throw new ApplicationException(UserMessage.INCORRECT_PASSWORD);
      }
    } catch (BadCredentialsException e) {
      throw new ApplicationException(UserMessage.USER_NOT_FOUND);
    }
  }
}
