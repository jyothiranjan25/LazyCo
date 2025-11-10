package com.example.lazyco.backend.core.WebMVC.Security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private UserDetailsService userDetailsService;

  @Autowired
  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String contextPath = request.getContextPath();
    String requestURI = request.getRequestURI();
    // Remove context path for endpoint matching
    String normalizedURI =
        requestURI.startsWith(contextPath)
            ? requestURI.substring(contextPath.length())
            : requestURI;
    // Always skip JWT logic for public endpoints and static resources, even if jwt_token cookie
    // exists
    if (isStaticResource(normalizedURI) || isPublicEndpoint(normalizedURI)) {
      logger.info("Skipping JWT filter for public/static endpoint: {}", normalizedURI);
      filterChain.doFilter(request, response);
      return;
    }
    try {
      // Check if authentication is already set to prevent duplicate processing
      if (SecurityContextHolder.getContext().getAuthentication() != null) {
        filterChain.doFilter(request, response);
        return;
      }

      String username = JwtUtil.extractUsername(request);
      if (username != null) {
        // Performance optimization: Use cached user details instead of repeated DB calls
        UserDetails userDetails = getCachedUserDetails(username);

        if (userDetails != null && JwtUtil.validateToken(request, username)) {
          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authenticationToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);

          logger.info("JWT authentication successful for user: {}", username);
        } else {
          logger.warn("JWT authentication failed for user: {}", username);
        }
      }

      filterChain.doFilter(request, response);
    } catch (JwtException e) {
      logger.error("JWT authentication error: {}", e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      //            SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();
      //            simpleResponseDTO.setMessage("Authentication failed: " + e.getMessage());
      //
      // response.getWriter().write(GsonSingleton.convertObjectToJSONString(simpleResponseDTO));
    } catch (Exception e) {
      logger.error("Unexpected error in JWT filter: {}", e.getMessage());
      // Continue with the filter chain even if JWT processing fails
      filterChain.doFilter(request, response);
    }
  }

  /**
   * Performance optimization: Check if the request is for static resources that don't need JWT
   * validation.
   */
  private boolean isStaticResource(String requestURI) {
    return requestURI.startsWith("/css/")
        || requestURI.startsWith("/js/")
        || requestURI.startsWith("/images/")
        || requestURI.startsWith("/static/")
        || requestURI.equals("/health")
        || requestURI.equals("/actuator/health");
  }

  /**
   * Performance optimization: Get user details with caching to prevent repeated database queries.
   */
  private UserDetails getCachedUserDetails(String username) {
    try {
      return userDetailsService.loadUserByUsername(username);
    } catch (Exception e) {
      return null;
    }
  }

  private boolean isPublicEndpoint(String requestURI) {
    // Adjust for context path
    return requestURI.startsWith("/user_verification/")
        || requestURI.startsWith("/course_requisite/")
        || requestURI.equals("/graduation_student_credential/get_by_uuid")
        || requestURI.equals("/department/get_without_login")
        || requestURI.startsWith("/connector/sso")
        || requestURI.startsWith("/loan/")
        || requestURI.startsWith("/project/kos_version")
        || requestURI.startsWith("/payment_gateway/validate_payment_url")
        || requestURI.startsWith("/ping")
        || requestURI.startsWith("/version");
  }
}
