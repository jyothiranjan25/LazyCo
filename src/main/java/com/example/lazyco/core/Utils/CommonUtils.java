package com.example.lazyco.core.Utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CommonUtils {

  public static boolean isValidEmail(String email) {
    if (email == null || email.isEmpty()) {
      return false;
    }
    String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    return email.matches(emailRegex);
  }

  public static String toSnakeCase(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    input = input.trim();

    // Insert underscore between lowercase and uppercase letter transitions
    String result =
        input.replaceAll("([a-z0-9])([A-Z])", "$1_$2").replaceAll("([A-Z])([A-Z][a-z])", "$1_$2");

    // Convert to lowercase
    result = result.toLowerCase();

    // Replace all non-alphanumeric characters with underscore
    result = result.replaceAll("[^a-z0-9]+", "_");

    // Replace multiple underscores with single underscore
    result = result.replaceAll("_+", "_");

    // Remove leading and trailing underscores
    result = result.replaceAll("^_+|_+$", "");

    return result;
  }
}
