package com.example.lazyco.core.WebMVC.Filter;

import com.example.lazyco.core.AbstractAction;
import com.example.lazyco.core.Logger.ApplicationLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestProcessingFilter extends OncePerRequestFilter {

  private final AbstractAction abstractAction;

  public RequestProcessingFilter(AbstractAction abstractAction) {
    this.abstractAction = abstractAction;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      if (request.getContentType() != null
          && !MediaType.MULTIPART_FORM_DATA.includes(
              MediaType.parseMediaType(request.getContentType()))) {

        // Wrap request and buffer body right here
        PreReadRequestWrapper wrappedRequest = new PreReadRequestWrapper(request);
        String body =
            new String(wrappedRequest.getRequestBody(), wrappedRequest.getCharacterEncoding());

        String fullUrl = request.getRequestURI();
        if (request.getQueryString() != null) {
          fullUrl += "?" + request.getQueryString();
        }
        ApplicationLogger.info(
            "processing Request{}METHOD: {}{}Content Type: {}{}URI: {}{}Body: {}",
            System.lineSeparator(),
            request.getMethod(),
            System.lineSeparator(),
            request.getContentType(),
            System.lineSeparator(),
            fullUrl,
            System.lineSeparator(),
            body);

        filterChain.doFilter(wrappedRequest, response);
      } else {
        ApplicationLogger.info(
            "processing Request{}METHOD: {}{}Content Type: {}{}URI: {}",
            System.lineSeparator(),
            request.getMethod(),
            System.lineSeparator(),
            request.getContentType(),
            System.lineSeparator(),
            request.getRequestURI());
        filterChain.doFilter(request, response);
      }
    } finally {
      // Clean up ThreadLocals and MDC (for safety)
      abstractAction.clearThreadLocals();
      MDC.clear();
    }
  }
}
