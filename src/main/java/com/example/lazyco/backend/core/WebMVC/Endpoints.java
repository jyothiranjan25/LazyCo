package com.example.lazyco.backend.core.WebMVC;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Endpoints {
  public List<String> getPublicEndpoints() {
    return List.of("/user/login", "/user/logout");
  }

  public List<String> getExcludedRoleCheckEndpoints() {
    List<String> excludedEndpoints = new ArrayList<>(getPublicEndpoints());
    excludedEndpoints.add("/user/set_role");
    excludedEndpoints.add("/user/reset_password");
    return excludedEndpoints;
  }
}
