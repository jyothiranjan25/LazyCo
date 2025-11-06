package com.example.lazyco.backend.core.WebMVC;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ThreadLocalCleanupFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } finally {
      ApplicationLogger.info("Request completed, cleaning up ThreadLocal variables");
      // Clean up ThreadLocal variables to prevent memory leaks
      AbstractAction.clearThreadLocals();
    }
  }
}
