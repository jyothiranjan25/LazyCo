package com.example.lazyco.entities.User;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractModelMapper;
import com.example.lazyco.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.core.Email.EmailDTO;
import com.example.lazyco.core.Email.EmailService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.core.Messages.CustomMessage;
import com.example.lazyco.core.Utils.CommonConstants;
import com.example.lazyco.core.WebMVC.RBSECHelper.BypassRBAC;
import com.example.lazyco.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.entities.UserManagement.AppUser.AppUserService;
import com.example.lazyco.entities.UserManagement.UserRole.UserRoleDTO;
import com.example.lazyco.entities.UserManagement.UserRole.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.jspecify.annotations.NonNull;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@BypassRBAC
public class UserService implements UserDetailsService {

  private AppUserService appUserService;
  private UserRoleService userRoleService;
  private EmailService emailService;
  private AuthenticationService authenticationService;
  private JwtUtil jwtUtil;

  @Autowired
  public void injectDependencies(
      AppUserService appUserService,
      UserRoleService userRoleService,
      EmailService emailService,
      @Lazy AuthenticationService authenticationService,
      @Lazy JwtUtil jwtUtil) {
    this.appUserService = appUserService;
    this.userRoleService = userRoleService;
    this.emailService = emailService;
    this.authenticationService = authenticationService;
    this.jwtUtil = jwtUtil;
  }

  @NonNull
  @Override
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    UserDTO user = getUser(username);
    if (user == null) {
      throw new UsernameNotFoundException(
          CustomMessage.getMessageString(UserMessage.USER_NOT_FOUND, username));
    }
    return user;
  }

  // Get User by Username
  public UserDTO getUser(String userName) {
    if (userName.isEmpty()) {
      return null;
    }
    AppUserDTO appUserDTO = appUserService.getUserByUserIdOrEmail(userName);
    return mapToUserDTO(appUserDTO);
  }

  // Get User by Id
  public AppUserDTO getUserById(Long id) {
    if (id == null) {
      return null;
    }
    return appUserService.getById(id);
  }

  // Map AppUserDTO to UserDTO
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

  // User Login
  public UserDTO login(UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
    userDTO = authenticationService.loginAndGetToken(userDTO);
    String token = userDTO.getToken();

    // Add token to HTTP-only cookie
    jwtUtil.addToCookie(response, token);

    // store last login Details
    AppUserDTO appUserDTO = new AppUserDTO();
    appUserDTO.setId(userDTO.getId());
    appUserDTO.setLastLoginIpAddress(request.getRemoteAddr());
    appUserDTO.setLastLoginDate(DateTimeZoneUtils.getCurrentDate());
    appUserService.update(appUserDTO);

    return userDTO;
  }

  // Get User Role
  public List<UserRoleDTO> getUserRole(HttpServletRequest request) {
    AppUserDTO longedUser = jwtUtil.checkAndGetUser(request, null);
    if (longedUser == null || longedUser.getId() == null) {
      throw new ApplicationException(UserMessage.USER_NOT_AUTHORIZED);
    }
    UserRoleDTO userRoleDTO = new UserRoleDTO();
    userRoleDTO.setAppUserId(longedUser.getId());
    List<UserRoleDTO> userRoleDTOS = userRoleService.get(userRoleDTO);
    if (userRoleDTOS == null || userRoleDTOS.isEmpty()) {
      throw new ApplicationException(UserMessage.ROLE_NOT_FOUND);
    }
    return userRoleDTOS;
  }

  // Set User Role
  public UserDTO setRole(
      UserRoleDTO userRoleDTO, HttpServletRequest request, HttpServletResponse response) {
    AppUserDTO longedUser = jwtUtil.checkAndGetUser(request, null);
    if (longedUser == null || longedUser.getId() == null) {
      throw new ApplicationException(UserMessage.USER_NOT_AUTHORIZED);
    }
    userRoleDTO.setFetchOnlyRole(true);
    userRoleDTO.setAppUserId(longedUser.getId());
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

  // Reset Password
  public UserDTO forgetPassword(UserDTO userDTO) {
    UserDTO user = getUser(userDTO.getUsername());
    if (user == null) {
      throw new ApplicationException(
          UserMessage.USER_NOT_FOUND, new Object[] {userDTO.getUsername()});
    }

    AppUserDTO appUserDTO = new AppUserDTO();
    appUserDTO.setId(user.getId());
    appUserDTO.setResetPasswordToken(generateResetToken());
    Date expiryDate =
        DateTimeZoneUtils.getCurrentDatePlus(Duration.ofMinutes(10)); // Token valid for 5 minutes
    appUserDTO.setResetPasswordTokenExpiry(expiryDate);
    AppUserDTO updated = appUserService.update(appUserDTO);

    // send reset token via email - skipped for now
    EmailDTO emailDTO = new EmailDTO();
    emailDTO.setSubject("Reset Password");
    emailDTO.setBodyText(
        "Your password reset token is: "
            + updated.getResetPasswordToken()
            + "\nThis token will expire in 10 minutes.");
    emailDTO.setTo(List.of(updated.getEmail()));
    emailService.sendSystemMail(emailDTO);

    user = new UserDTO();
    user.setMessage(CustomMessage.getMessageString(UserMessage.PASSWORD_RESET_INITIATED));

    return user;
  }

  public UserDTO resetPassword(UserDTO userDTO, HttpServletResponse response) {
    AppUserDTO appUser = appUserService.getUserByUserIdOrEmail(userDTO.getUsername());
    if (appUser == null) {
      throw new ApplicationException(
          UserMessage.USER_NOT_FOUND, new Object[] {userDTO.getUsername()});
    }
    if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
      throw new ApplicationException(UserMessage.PASSWORD_CANNOT_BE_EMPTY);
    }
    if (appUser.getResetPasswordToken() == null
        || !appUser.getResetPasswordToken().equals(userDTO.getToken())) {
      throw new ApplicationException(UserMessage.INVALID_RESET_TOKEN);
    }
    Date now = DateTimeZoneUtils.getCurrentDate();
    if (appUser.getResetPasswordTokenExpiry() == null
        || appUser.getResetPasswordTokenExpiry().before(now)) {
      throw new ApplicationException(UserMessage.RESET_TOKEN_EXPIRED);
    }
    // Update password and clear reset token
    appUser.setPassword(userDTO.getPassword());
    appUser.setResetPasswordToken(null);
    appUser.setResetPasswordTokenExpiry(null);
    appUserService.update(appUser);

    // Auto-login after password reset
    UserDTO user = new UserDTO();
    user.setUsername(userDTO.getUsername());
    user.setPassword(userDTO.getPassword());
    user = authenticationService.loginAndGetToken(user);
    String token = user.getToken();
    jwtUtil.clearCookie(response);
    jwtUtil.addToCookie(response, token);
    user.setMessage(CustomMessage.getMessageString(UserMessage.PASSWORD_RESET_SUCCESS));
    return user;
  }

  public String generateResetToken() {
    String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    int CODE_LENGTH = 6;
    SecureRandom secureRandom = new SecureRandom();
    StringBuilder code = new StringBuilder(CODE_LENGTH);
    for (int i = 0; i < CODE_LENGTH; i++) {
      int index = secureRandom.nextInt(CHARACTERS.length());
      code.append(CHARACTERS.charAt(index));
    }
    return code.toString();
  }

  // User Logout
  public SimpleResponseDTO logout(HttpServletResponse response) {
    // Clear the JWT cookie
    jwtUtil.clearCookie(response);

    SimpleResponseDTO responseDTO = new SimpleResponseDTO();
    responseDTO.setMessage(CustomMessage.getMessageString(UserMessage.LOGOUT_SUCCESS));
    return responseDTO;
  }
}
