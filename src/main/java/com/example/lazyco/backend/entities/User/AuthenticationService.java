package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
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
      UserDTO user = (UserDTO) authentication.getPrincipal();
      if (authentication.isAuthenticated()) {
        return jwtUtil.generateToken(user.getUsername());
      } else {
        throw new ExceptionWrapper(UserMessage.INCORRECT_PASSWORD);
      }
    } catch (BadCredentialsException e) {
      throw new ExceptionWrapper(UserMessage.USER_ID_OR_PASSWORD_INCORRECT);
    }
  }
}
