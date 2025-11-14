package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractModelMapper;
import com.example.lazyco.backend.core.Exceptions.ApplicationException;
import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
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

  private final AuthenticationService authenticationService;
  private final UserRoleService userRoleService;
  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
    userDTO = authenticationService.loginAndGetToken(userDTO);
    String token = userDTO.getToken();

    // Add token to HTTP-only cookie
    jwtUtil.addToCookie(response, token);

    return ResponseUtils.sendResponse(userDTO);
  }

  @PostMapping("/set_role")
  public ResponseEntity<?> setRole(
      @RequestBody UserRoleDTO userRoleDTO,
      HttpServletRequest request,
      HttpServletResponse response) {
    userRoleDTO.setFetchOnlyRole(true);
    userRoleDTO = userRoleService.getSingle(userRoleDTO);
    if (userRoleDTO == null) {
      throw new ApplicationException(UserMessage.ROLE_NOT_FOUND);
    }
    String token =
        jwtUtil.addClaimsAndRegenerateToken(
            Map.of(CommonConstants.LOGGED_USER_ROLE, userRoleDTO.getId()), request);

    // Add token to HTTP-only cookie
    jwtUtil.addToCookie(response, token);

    UserDTO userDTO = new UserDTO();
    if (userRoleDTO.getAppUser() != null) {
      userDTO =
          new AbstractModelMapper()
              .map(
                  userRoleDTO.getAppUser(),
                  userDTO,
                  typeMap -> {
                    typeMap.addMappings(
                        mapper -> {
                          mapper.map(AppUserDTO::getUserId, UserDTO::setUsername);
                        });
                  });
    }
    userDTO.setToken(token);
    userDTO.setRole(userRoleDTO.getRole());
    return ResponseUtils.sendResponse(userDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<SimpleResponseDTO> logout(HttpServletResponse response) {
    // Clear the JWT cookie
    jwtUtil.clearCookie(response);

    SimpleResponseDTO responseDTO = new SimpleResponseDTO();
    responseDTO.setMessage("Logged out successfully");
    return ResponseUtils.sendResponse(responseDTO);
  }
}
