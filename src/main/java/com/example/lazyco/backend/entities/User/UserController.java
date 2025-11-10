package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private final AuthenticationService authenticationService;
  private final JwtUtil jwtUtil;

  public UserController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
    this.authenticationService = authenticationService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
    String token = authenticationService.loginAndGetToken(userDTO);

    // Add token to HTTP-only cookie
    jwtUtil.addToCookie(response, token);

    // Set token in DTO for response (optional - if frontend needs it)
    userDTO.setToken(token);
    userDTO.setPassword(null); // Don't send password back

    return ResponseEntity.ok(userDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<SimpleResponseDTO> logout(HttpServletResponse response) {
    // Clear the JWT cookie
    jwtUtil.clearCookie(response);

    // Clear session data
    jwtUtil.clearSessionData();

    SimpleResponseDTO responseDTO = new SimpleResponseDTO();
    responseDTO.setMessage("Logged out successfully");
    return ResponseEntity.ok(responseDTO);
  }
}
