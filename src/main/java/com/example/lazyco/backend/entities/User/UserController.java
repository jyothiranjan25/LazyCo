package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(
      @RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
    userDTO = userService.login(userDTO, request, response);
    return ResponseUtils.sendResponse(userDTO);
  }

  @GetMapping("/get_user_role")
  public ResponseEntity<?> getUser(HttpServletRequest request) {
    UserRoleDTO userRoleDTO = new UserRoleDTO();
    userRoleDTO.setObjects(userService.getUserRole(request));
    return ResponseUtils.sendResponse(userRoleDTO);
  }

  @PostMapping("/set_role")
  public ResponseEntity<?> setRole(
      @RequestBody UserRoleDTO userRoleDTO,
      HttpServletRequest request,
      HttpServletResponse response) {
    UserDTO userDTO = userService.setRole(userRoleDTO, request, response);
    return ResponseUtils.sendResponse(userDTO);
  }

  @PostMapping("/forgot_password")
  public ResponseEntity<UserDTO> forgetPassword(@RequestBody UserDTO userDTO) {
    userDTO = userService.forgetPassword(userDTO);
    return ResponseUtils.sendResponse(userDTO);
  }

  @PostMapping("/reset_password")
  public ResponseEntity<UserDTO> resetPassword(
      @RequestBody UserDTO userDTO, HttpServletResponse response) {
    userDTO = userService.resetPassword(userDTO, response);
    return ResponseUtils.sendResponse(userDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<SimpleResponseDTO> logout(HttpServletResponse response) {
    SimpleResponseDTO responseDTO = userService.logout(response);
    return ResponseUtils.sendResponse(responseDTO);
  }
}
