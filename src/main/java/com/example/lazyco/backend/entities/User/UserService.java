package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractModelMapper;
import com.example.lazyco.backend.core.Exceptions.ApplicationException;
import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.backend.core.Messages.CustomMessage;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserService;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.function.Consumer;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private AppUserService appUserService;
  private UserRoleService userRoleService;
  private AbstractAction abstractAction;
  private AuthenticationService authenticationService;
  private JwtUtil jwtUtil;

  @Autowired
  public void injectDependencies(
      AppUserService appUserService,
      UserRoleService userRoleService,
      AbstractAction abstractAction,
      @Lazy AuthenticationService authenticationService,
      @Lazy JwtUtil jwtUtil) {
    this.appUserService = appUserService;
    this.abstractAction = abstractAction;
    this.authenticationService = authenticationService;
    this.userRoleService = userRoleService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDTO user = getUser(username);
    if (user == null) {
      throw new UsernameNotFoundException(
          CustomMessage.getMessageString(UserMessage.USER_NOT_FOUND, username));
    }
    return user;
  }

  public UserDTO getUser(String userName) {
    abstractAction.setBypassRBAC(true);
    try {
      if (userName.isEmpty()) {
        return null;
      }
      AppUserDTO appUserDTO = appUserService.getUserByUserIdOrEmail(userName);
      return mapToUserDTO(appUserDTO);
    } finally {
      abstractAction.setBypassRBAC(false);
    }
  }

  public AppUserDTO getUserById(Long id) {
    abstractAction.setBypassRBAC(true);
    try {
      if (id == null) {
        return null;
      }
      return appUserService.getById(id);
    } finally {
      abstractAction.setBypassRBAC(false);
    }
  }

  public UserDTO mapToUserDTO(AppUserDTO appUserDTO) {
    if (appUserDTO == null) {
      return null;
    }
    return new AbstractModelMapper()
        .map(
            appUserDTO,
            UserDTO.class,
            (Consumer<TypeMap<AppUserDTO, UserDTO>>)
                typeMap -> {
                  typeMap.addMappings(
                      mapper -> {
                        mapper.map(AppUserDTO::getUserId, UserDTO::setUsername);
                      });
                });
  }

  public UserDTO login(UserDTO userDTO, HttpServletResponse response) {
    userDTO = authenticationService.loginAndGetToken(userDTO);
    String token = userDTO.getToken();

    // Add token to HTTP-only cookie
    jwtUtil.addToCookie(response, token);
    return userDTO;
  }

  public UserDTO setRole(
      UserRoleDTO userRoleDTO, HttpServletRequest request, HttpServletResponse response) {
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
      userDTO = mapToUserDTO(userRoleDTO.getAppUser());
    }
    userDTO.setToken(token);
    userDTO.setRole(userRoleDTO.getRole());
    return userDTO;
  }

  public SimpleResponseDTO logout(HttpServletResponse response) {
    // Clear the JWT cookie
    jwtUtil.clearCookie(response);

    SimpleResponseDTO responseDTO = new SimpleResponseDTO();
    responseDTO.setMessage(CustomMessage.getMessageString(UserMessage.LOGOUT_SUCCESS));
    return responseDTO;
  }
}
