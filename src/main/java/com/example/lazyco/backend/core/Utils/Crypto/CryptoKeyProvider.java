package com.example.lazyco.backend.core.Utils.Crypto;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoKeyProvider {

  private static final String KEY_FILE_PATH = CommonConstants.TOMCAT_TEMP + "configSecret.key";

  public static SecretKey loadOrCreateSecretKey() {
    try {
      Path keyFilePath = Paths.get(KEY_FILE_PATH);
      Path keyFolderPath = Paths.get(CommonConstants.TOMCAT_TEMP);

      // If key exists, load it
      if (Files.exists(keyFilePath)) {
        String base64Key = Files.readString(keyFilePath).trim();
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(keyBytes, "AES");
      }

      ApplicationLogger.warn(
          "Configuration Master AES key missing — generating a NEW key. All previously encrypted data will become unreadable.");

      // Ensure folder exists
      Files.createDirectories(keyFolderPath);

      // Generate AES-256 key
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      keyGen.init(256);
      SecretKey newKey = keyGen.generateKey();

      String base64Key = Base64.getEncoder().encodeToString(newKey.getEncoded());
      Files.write(keyFilePath, base64Key.getBytes());

      return newKey;

    } catch (IOException | NoSuchAlgorithmException e) {
      throw new RuntimeException("Failed to load or create AES configuration secret key", e);
    }
  }
}
