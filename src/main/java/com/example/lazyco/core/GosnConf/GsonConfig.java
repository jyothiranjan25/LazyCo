package com.example.lazyco.core.GosnConf;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {
  @Bean
  public Gson gson() {
    return GsonSingleton.getInstance();
  }
}
