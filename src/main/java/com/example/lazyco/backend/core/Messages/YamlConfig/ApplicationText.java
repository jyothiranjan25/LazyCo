package com.example.lazyco.backend.core.Messages.YamlConfig;

public class ApplicationText {
  public static String get(String key, Language language) {
    String fileName = getFileByLanguage(language);
    // @Todo: change null to language if needed in future
    return YamlUtils.getValueForKey(key, fileName, null);
  }

  // add switch case to get file by language
  private static String getFileByLanguage(Language language) {
    return switch (language) {
      case ES, FR, DE -> "application_text_" + language.name().toLowerCase() + ".yaml";
      default -> "application_text.yaml";
    };
  }

  public enum Language {
    EN,
    ES,
    FR,
    DE;

    public static Language fromString(String value) {
      for (Language lang : values()) {
        if (lang.name().equalsIgnoreCase(value)) {
          return lang;
        }
      }
      return EN; // Default to English if no match found
    }
  }
}
