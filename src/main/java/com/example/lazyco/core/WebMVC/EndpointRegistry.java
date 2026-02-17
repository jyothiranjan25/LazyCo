package com.example.lazyco.core.WebMVC;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class EndpointRegistry {

  private final Map<EndpointType, List<String>> endpointMap = new EnumMap<>(EndpointType.class);

  private final AntPathMatcher matcher = new AntPathMatcher();

  public EndpointRegistry() {

    /** Define AUTH endpoints that are related to authentication and user management. */
    List<String> authEndpoints = new ArrayList<>();
    authEndpoints.add("/user/login");
    authEndpoints.add("/user/forgot_password");
    authEndpoints.add("/user/reset_password");
    authEndpoints.add("/user/logout");
    authEndpoints.add("/user/get_user_role");
    authEndpoints.add("/user/set_role");
    endpointMap.put(EndpointType.AUTH, authEndpoints);

    /** Define SECURITY endpoints that are related to security and access control. */
    List<String> securityEndpoints = new ArrayList<>();
    securityEndpoints.add("/admin/**");
    endpointMap.put(EndpointType.SECURITY, securityEndpoints);

    /** Define PUBLIC endpoints that are accessible without authentication. */
    List<String> publicEndpoints = new ArrayList<>();
    endpointMap.put(EndpointType.PUBLIC, publicEndpoints);

    /** Define INTERNAL endpoints that are used for internal communication. */
    endpointMap.put(EndpointType.INTERNAL, List.of("/**"));
  }

  public EndpointType resolve(HttpServletRequest request) {

    String uri = request.getRequestURI();
    String contextPath = request.getContextPath();

    if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
      uri = uri.substring(contextPath.length());
    }

    for (Map.Entry<EndpointType, List<String>> entry : endpointMap.entrySet()) {
      for (String pattern : entry.getValue()) {
        if (matcher.match(pattern, uri)) {
          return entry.getKey();
        }
      }
    }

    return EndpointType.INTERNAL; // default fallback
  }

  public enum EndpointType {
    AUTH,
    SECURITY,
    PUBLIC,
    INTERNAL
  }

  // for Interceptor use
  public List<String> getPublicEndpoints() {
    List<String> endpoints = new ArrayList<>();
    endpoints.add("/user/login");
    endpoints.add("/user/logout");
    endpoints.add("/user/forgot_password");
    endpoints.add("/user/reset_password");
    endpoints.add("/admin/**");
    return endpoints;
  }

  public List<String> getExcludedRoleCheckEndpoints() {
    List<String> excludedEndpoints = new ArrayList<>(getPublicEndpoints());
    excludedEndpoints.add("/user/get_user_role");
    excludedEndpoints.add("/user/set_role");
    return excludedEndpoints;
  }
}
