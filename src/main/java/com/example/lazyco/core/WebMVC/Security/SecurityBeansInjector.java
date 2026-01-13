package com.example.lazyco.core.WebMVC.Security;

import com.example.lazyco.entities.User.CustomPasswordEncoder;
import com.example.lazyco.entities.User.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class SecurityBeansInjector {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new CustomPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(
      UserService userService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) {
    return authenticationConfiguration
        .getAuthenticationManager(); // providerManager implements AuthenticationManager
  }
}
