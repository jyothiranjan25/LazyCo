package com.example.lazyco.core.WebMVC.Interceptor;

import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.CommonConstants;
import com.example.lazyco.entities.User.JwtUtil;
import com.example.lazyco.entities.User.UserMessage;
import com.example.lazyco.entities.UserManagement.AppUser.AppUserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginControllerInterceptor implements HandlerInterceptor {

  private final JwtUtil jwtUtil;

  public LoginControllerInterceptor(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    ApplicationLogger.info("Checking user is logged in - LoginControllerInterceptor");

    // Case 1: Check if the request is invalid or the user is not logged in
    if (jwtUtil.requestIsInvalid(request, CommonConstants.LOGGED_USER)) {
      request.getSession().invalidate();
      throw new ApplicationException(
          HttpStatus.UNAUTHORIZED, UserMessage.USER_NOT_AUTHORIZED); // Stop further processing
    }

    AppUserDTO loggedUser = jwtUtil.getLoggedUser(request);

    // Case 2: Check if the user account is Active
    if (Boolean.FALSE.equals(loggedUser.getIsActive())) {
      request.getSession().invalidate();
      throw new ApplicationException(
          HttpStatus.UNAUTHORIZED, UserMessage.ACCOUNT_DISABLED); // Stop further processing
    }

    // Case 3: Check if the user account is locked
    if (Boolean.TRUE.equals(loggedUser.getIsLocked())) {
      request.getSession().invalidate();
      throw new ApplicationException(
          HttpStatus.UNAUTHORIZED, UserMessage.ACCOUNT_LOCKED); // Stop further processing
    }

    /*
     * Push the request URL to session. It would be required for book marking pages.
     */
    String currentAction = request.getServletPath() + "?" + request.getQueryString();
    request.getSession().setAttribute("CURRENT_ACTION_URL", currentAction);

    return true; // Return true to continue processing the request
  }
}
