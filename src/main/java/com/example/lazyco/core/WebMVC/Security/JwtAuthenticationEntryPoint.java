package com.example.lazyco.core.WebMVC.Security;

import com.example.lazyco.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.core.GosnConf.GsonSingleton;
import com.example.lazyco.core.Logger.ApplicationLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();
    simpleResponseDTO.setMessage(authException.getMessage());

    response.getWriter().write(GsonSingleton.convertObjectToJSONString(simpleResponseDTO));
    ApplicationLogger.info(authException.getMessage());
  }

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    SimpleResponseDTO responseDTO = new SimpleResponseDTO();
    responseDTO.setMessage("Forbidden: " + accessDeniedException.getMessage());

    response.getWriter().write(GsonSingleton.convertObjectToJSONString(responseDTO));
    ApplicationLogger.warn(accessDeniedException.getMessage());
  }
}
