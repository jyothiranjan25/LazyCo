package com.example.lazyco.backend.core.WebMVC;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PublicEndpoints {
  public List<String> getEndpoints() {
    return List.of("/user/**", "/app_user/**");
  }
}
