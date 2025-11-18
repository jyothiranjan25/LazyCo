package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

  private static final SecretKey secretKey = CryptoKeyProvider.loadOrCreateSecretKey();

  private static final int IV_SIZE = 12; // 12 bytes for GCM
  private static final int TAG_SIZE = 128; // 128-bit auth tag

  // Encrypt a string
  public static String encrypt(String plainText) {
    try {
      byte[] keyBytes = secretKey.getEncoded();
      SecretKey key = new SecretKeySpec(keyBytes, "AES");

      byte[] iv = new byte[IV_SIZE];
      SecureRandom random = new SecureRandom();
      random.nextBytes(iv);

      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
      cipher.init(Cipher.ENCRYPT_MODE, key, spec);

      byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

      // Store IV + ciphertext together
      byte[] combined = new byte[iv.length + cipherText.length];
      System.arraycopy(iv, 0, combined, 0, iv.length);
      System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

      return Base64.getEncoder().encodeToString(combined);
    } catch (Exception e) {
      ApplicationLogger.error("Encryption failed: " + e.getMessage());
      return null;
    }
  }

  // Decrypt a string
  public static String decrypt(String encryptedBase64) {
    try {
      byte[] keyBytes = secretKey.getEncoded();
      byte[] combined = Base64.getDecoder().decode(encryptedBase64);

      byte[] iv = new byte[IV_SIZE];
      byte[] cipherText = new byte[combined.length - IV_SIZE];

      System.arraycopy(combined, 0, iv, 0, IV_SIZE);
      System.arraycopy(combined, IV_SIZE, cipherText, 0, cipherText.length);

      SecretKey key = new SecretKeySpec(keyBytes, "AES");

      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
      cipher.init(Cipher.DECRYPT_MODE, key, spec);

      byte[] plainText = cipher.doFinal(cipherText);

      return new String(plainText, StandardCharsets.UTF_8);
    } catch (Exception e) {
      ApplicationLogger.error("Decryption failed: " + e.getMessage());
      return null;
    }
  }
}
