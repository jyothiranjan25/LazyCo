package com.example.lazyco.backend.core.messages.yamlconfig;

public class ApplicationText {
    public static String get(String key, Language language) {
        String fileName = getFileByLanguage(language);
        return YamlUtils.getValueForKey(key, fileName);
    }

    // add switch case to get file by language
    private static String getFileByLanguage(Language language) {
        return switch (language) {
            case EN, ES -> "application_text_" + language.name().toLowerCase() + ".yaml";
            default -> "application_text.yaml";
        };
    }


    public enum Language {
        EN, ES;

        public static Language fromString(String value) {
            for (Language lang : values()) {
                if (lang.name().equalsIgnoreCase(value)) {
                    return lang;
                }
            }
            return EN; // fallback default
        }
    }
}