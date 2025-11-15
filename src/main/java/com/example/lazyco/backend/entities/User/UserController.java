package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
    userDTO = userService.login(userDTO, response);
    return ResponseUtils.sendResponse(userDTO);
  }

  @PostMapping("/set_role")
  public ResponseEntity<?> setRole(
      @RequestBody UserRoleDTO userRoleDTO,
      HttpServletRequest request,
      HttpServletResponse response) {
    UserDTO userDTO = userService.setRole(userRoleDTO, request, response);
    return ResponseUtils.sendResponse(userDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<SimpleResponseDTO> logout(HttpServletResponse response) {
    SimpleResponseDTO responseDTO = userService.logout(response);
    return ResponseUtils.sendResponse(responseDTO);
  }
}
