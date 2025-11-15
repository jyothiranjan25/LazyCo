package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public UserDTO loginAndGetToken(UserDTO userDTO) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  userDTO.getUsername(), userDTO.getPassword()));
      UserDTO user = (UserDTO) authentication.getPrincipal();
      if (authentication.isAuthenticated()) {
        String token =
            jwtUtil.generateToken(
                user.getUsername(), Map.of(CommonConstants.LOGGED_USER, user.getId()));
        user.setToken(token);
        return user;
      } else {
        throw new ExceptionWrapper(UserMessage.INCORRECT_PASSWORD);
      }
    } catch (BadCredentialsException e) {
      throw new ExceptionWrapper(UserMessage.USER_ID_OR_PASSWORD_INCORRECT);
    } catch (LockedException e) {
      throw new ExceptionWrapper(UserMessage.ACCOUNT_LOCKED);
    } catch (DisabledException e) {
      throw new ExceptionWrapper(UserMessage.ACCOUNT_DISABLED);
    }
  }
}
