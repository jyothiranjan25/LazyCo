package com.example.lazyco.core.WebMVC.Interceptor;

import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.CommonConstants;
import com.example.lazyco.backend.entities.User.JwtUtil;
import com.example.lazyco.backend.entities.User.UserMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleControllerInterceptor implements HandlerInterceptor {

  private final JwtUtil jwtUtil;

  public RoleControllerInterceptor(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    ApplicationLogger.info("Check user role - RoleControllerInterceptor");

    // Case 1: Check if the user is not null
    if (jwtUtil.requestIsInvalid(request, CommonConstants.LOGGED_USER_ROLE)) {
      request.getSession().invalidate();
      throw new ApplicationException(
          HttpStatus.METHOD_NOT_ALLOWED, UserMessage.ROLE_NOT_SELECTED); // Stop further processing
    }

    /*
     * Push the request URL to session. It would be required for book marking pages.
     */
    String currentAction = request.getServletPath() + "?" + request.getQueryString();
    request.getSession().setAttribute("CURRENT_ACTION_URL", currentAction);

    return true; // Return true to continue processing the request
  }
}
