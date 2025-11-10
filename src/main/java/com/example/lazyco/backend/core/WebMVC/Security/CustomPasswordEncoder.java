package com.example.lazyco.backend.core.WebMVC.Security;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
  @Override
  public String encode(CharSequence rawPassword) {
    return encode(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    // Compare the encoded versions of the passwords
    return encode(rawPassword).equals(encodedPassword);
  }

  public static String encode(String rawPassword) {
    String encoded = null;
    try {
      byte[] secret = generate(normalize(rawPassword));
      encoded = Base64.getEncoder().encodeToString(secret);
    } catch (Exception e) {
      ApplicationLogger.error(e);
    }
    return encoded;
  }

  private static byte[] generate(String password) throws NoSuchAlgorithmException {
    MessageDigest d = null;
    d = MessageDigest.getInstance("SHA-1");
    d.reset();
    d.update(password.getBytes());
    return d.digest();
  }

  public static String normalize(String aSource) {

    return ((aSource == null) ? "" : aSource.trim());
  }
}
