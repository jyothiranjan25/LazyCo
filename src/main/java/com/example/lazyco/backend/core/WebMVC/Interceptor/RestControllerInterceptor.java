package com.example.lazyco.backend.core.WebMVC.Interceptor;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RestControllerInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    ApplicationLogger.info(
        "PreHandling request [method={}, uri={}]", request.getMethod(), request.getRequestURI());
    return true; // Return true to continue processing the request
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    ApplicationLogger.info(
        "PostHandling request [method={}, uri={}]", request.getMethod(), request.getRequestURI());
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // You can add custom logic here to be executed after the complete request has finished
    // For example, cleaning up resources or logging response details
    ApplicationLogger.info(
        "AfterCompletion request [method={}, uri={}]",
        request.getMethod(),
        request.getRequestURI());
  }
}
