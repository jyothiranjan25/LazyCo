package com.example.lazyco.backend.core.WebMVC.Security;

import com.example.lazyco.backend.entities.User.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class SecurityBeansInjector {

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserService();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    /*
     * Using CustomPasswordEncoder instead of Spring's default BCryptPasswordEncoder.
     *
     * Note:
     * - The CustomPasswordEncoder uses our own PasswordEncryptor for encoding passwords.
     * - This approach allows us to use existing passwords that are encoded with the custom algorithm.
     *
     * If we were to switch to BCryptPasswordEncoder:
     * - We would need to re-encode all existing user passwords with BCryptPasswordEncoder.
     * - This typically involves creating new user entries or updating the password hashes for all users.
     * - BCryptPasswordEncoder provides strong hashing, which is a recommended approach for password security.
     *
     * For now, we continue using CustomPasswordEncoder to maintain compatibility with our current password storage format.
     */

    return new CustomPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration
        .getAuthenticationManager(); // providerManager implements AuthenticationManager
  }
}
