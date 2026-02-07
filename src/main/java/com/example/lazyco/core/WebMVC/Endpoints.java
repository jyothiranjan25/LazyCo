package com.example.lazyco.core.WebMVC;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Endpoints {
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
