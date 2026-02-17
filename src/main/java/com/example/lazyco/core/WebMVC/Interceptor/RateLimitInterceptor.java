package com.example.lazyco.core.WebMVC.Interceptor;

import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.core.GosnConf.GsonSingleton;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.RateLimiter.RateLimiter;
import com.example.lazyco.core.Utils.CommonConstants;
import com.example.lazyco.core.WebMVC.EndpointRegistry;
import com.example.lazyco.entities.User.JwtUtil;
import com.example.lazyco.entities.User.UserMessage;
import com.example.lazyco.entities.UserManagement.AppUser.AppUserDTO;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

  private final EndpointRegistry endpointRegistry;
  private final RateLimiter rateLimiter;
  private final JwtUtil jwtUtil;

  public RateLimitInterceptor(
      EndpointRegistry endpointRegistry, JwtUtil jwtUtil, RateLimiter rateLimiter) {
    this.endpointRegistry = endpointRegistry;
    this.rateLimiter = rateLimiter;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    EndpointRegistry.EndpointType type = endpointRegistry.resolve(request);
    String uri = request.getRequestURI();
    switch (type) {
      case AUTH -> {
        String ip = getClientIp(request);
        ConsumptionProbe probe = rateLimiter.tryConsumeLogin(ip);

        if (!probe.isConsumed()) {
          return reject(response, probe, "LOGIN", ip, uri);
        }
      }

      case SECURITY -> {
        String ip = getClientIp(request);
        ConsumptionProbe probe = rateLimiter.tryConsumeSecurity(ip);

        if (!probe.isConsumed()) {
          return reject(response, probe, "SECURITY", ip, uri);
        }
      }

      case PUBLIC -> {
        String ip = getClientIp(request);
        ConsumptionProbe probe = rateLimiter.tryConsumePublic(ip);

        if (!probe.isConsumed()) {
          return reject(response, probe, "PUBLIC", ip, uri);
        }
      }

      case INTERNAL -> {
        if (jwtUtil.requestIsInvalid(request, CommonConstants.LOGGED_USER)) {
          request.getSession().invalidate();
          throw new ApplicationException(HttpStatus.UNAUTHORIZED, UserMessage.USER_NOT_AUTHORIZED);
        }

        AppUserDTO loggedUser = jwtUtil.getLoggedUser(request);
        String userKey = String.valueOf(loggedUser.getId());

        ConsumptionProbe probe = rateLimiter.tryConsumeInternal(userKey);

        if (!probe.isConsumed()) {
          return reject(response, probe, "INTERNAL", userKey, uri);
        }

        String currentAction =
            request.getServletPath()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        request.getSession().setAttribute("CURRENT_ACTION_URL", currentAction);
      }
    }
    return true;
  }

  private boolean reject(
      HttpServletResponse response, ConsumptionProbe probe, String type, String key, String uri)
      throws Exception {

    long waitSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;

    ApplicationLogger.warn(
        "RateLimitInterceptor - BLOCKED | type="
            + type
            + " | key="
            + key
            + " | uri="
            + uri
            + " | retryAfter="
            + waitSeconds
            + "s");

    response.setStatus(429);
    //    response.setHeader("Retry-After", String.valueOf(waitSeconds));
    SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();
    simpleResponseDTO.setMessage("Too many requests. Please try again after some time.");
    response.getWriter().write(GsonSingleton.convertObjectToJSONString(simpleResponseDTO));
    return false;
  }

  private String getClientIp(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");

    if (xfHeader == null || xfHeader.isBlank()) {
      return request.getRemoteAddr();
    }

    return xfHeader.split(",")[0].trim();
  }
}
