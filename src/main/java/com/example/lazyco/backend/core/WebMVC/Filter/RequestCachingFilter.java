package com.example.lazyco.backend.core.WebMVC.Filter;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class RequestCachingFilter extends OncePerRequestFilter {

  private final AbstractAction abstractAction;

  public RequestCachingFilter(AbstractAction abstractAction) {
    this.abstractAction = abstractAction;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    ApplicationLogger.info(
        "Starting request processing [method={}, uri={}]",
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
      String body =
          new String(cachingRequest.getContentAsByteArray(), cachingRequest.getCharacterEncoding());
      String fullUrl = request.getRequestURI();
      if (request.getQueryString() != null) {
        fullUrl += "?" + request.getQueryString();
      }
      ApplicationLogger.info(
          "Request Method: {}, URI: {}{}Body: {}",
          request.getMethod(),
          fullUrl,
          System.lineSeparator(),
          body);
      ApplicationLogger.info(
          "Completed request processing [method={}, uri={}]",
          request.getMethod(),
          request.getRequestURI());
      // Clean up ThreadLocals and MDC (for safety)
      abstractAction.clearThreadLocals();
      MDC.clear();
    }
  }
}
