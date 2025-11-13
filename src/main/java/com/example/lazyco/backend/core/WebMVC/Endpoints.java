package com.example.lazyco.backend.core.WebMVC;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Endpoints {
  public List<String> getPublicEndpoints() {
    return List.of("/user/**", "/app_user/**");
  }
}
