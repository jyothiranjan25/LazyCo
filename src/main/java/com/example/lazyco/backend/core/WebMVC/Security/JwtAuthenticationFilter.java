package com.example.lazyco.backend.core.WebMVC.Security;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.entities.User.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String token = jwtUtil.extractTokenFromRequest(request);

      if (token != null && jwtUtil.validateToken(token)) {
        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
        ApplicationLogger.info(
            "JWT Token validated for user [{}] for the request [method={}, uri={}]",
            username,
            request.getMethod(),
            request.getRequestURI());
      } else {
        ApplicationLogger.info(
            "JWT Token is missing for the request [method={}, uri={}]",
            request.getMethod(),
            request.getRequestURI());
      }
    } catch (Exception e) {
      ApplicationLogger.error("Could not set user authentication in security context", e);
      SecurityContextHolder.clearContext();
    } finally {
      filterChain.doFilter(request, response);
    }
  }
}
