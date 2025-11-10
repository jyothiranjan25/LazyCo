package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Exceptions.UnauthorizedException;
import com.example.lazyco.backend.core.WebMVC.Security.JwtUtil;
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

  public String loginAndGetToken(AuthenticationRequestDTO authenticationRequestDTO) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  authenticationRequestDTO.getUsername(), authenticationRequestDTO.getSecretKey()));
      if (authentication.isAuthenticated()) {
        return JwtUtil.generateToken(authenticationRequestDTO.getUsername());
      } else {
        throw new UnauthorizedException(UserMessage.INCORRECT_PASSWORD);
      }
    } catch (BadCredentialsException e) {
      throw new UnauthorizedException(UserMessage.USER_NOT_FOUND);
    }
  }
}
