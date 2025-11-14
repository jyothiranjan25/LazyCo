package com.example.lazyco.backend.core.WebMVC.Filter;

import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class RequestCachingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    ApplicationLogger.info(
        "Starting request processing Method: {}, URI: {}",
        request.getMethod(),
        request.getRequestURI());
    ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
    try {
      filterChain.doFilter(cachingRequest, response);
    } catch (Exception e) {
      ApplicationLogger.error(
          "Error during caching request [method={}, uri={}]",
          request.getMethod(),
          request.getRequestURI());

      // Handle exception and send error response
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();
      simpleResponseDTO.setMessage(e.getMessage());

      response.getWriter().write(GsonSingleton.convertObjectToJSONString(simpleResponseDTO));
      response.getWriter().flush();
      return;
    } finally {
      ApplicationLogger.info(
          "Completed request processing Method: {}, URI: {}",
          request.getMethod(),
          request.getRequestURI());
    }
  }
}
