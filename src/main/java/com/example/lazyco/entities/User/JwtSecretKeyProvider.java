package com.example.lazyco.backend.entities.User;

import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.CommonConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.crypto.SecretKey;

public class JwtSecretKeyProvider {
  private static final String KEY_FILE_PATH = CommonConstants.TOMCAT_TEMP + "secret.key";

  public static SecretKey loadOrCreateSecretKey() {
    try {
      Path keyFilePath = Paths.get(KEY_FILE_PATH);
      Path keyFolderPath = Paths.get(CommonConstants.TOMCAT_TEMP);

      // Create the key folder if it doesn't exist
      if (Files.exists(keyFilePath)) {
        // Read and decode the existing key
        String base64Key = new String(Files.readAllBytes(keyFilePath)).trim();
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Key));
      }
      ApplicationLogger.warn(
          "JWT secret file was missing — a new key has been generated. All existing tokens are now invalid!");

      // Create a folder if it doesn't exist
      Files.createDirectories(keyFolderPath);

      // Generate a new key
      SecretKey newKey = Jwts.SIG.HS256.key().build();
      String base64Key = Encoders.BASE64.encode(newKey.getEncoded());

      // Write key to file
      Files.write(keyFilePath, base64Key.getBytes());

      return newKey;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load or create JWT secret key", e);
    }
  }
}
