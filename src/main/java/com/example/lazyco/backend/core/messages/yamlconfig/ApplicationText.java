package com.example.lazyco.backend.core.messages.yamlconfig;

public class ApplicationText {
    public static String get(String key, Language language) {
        String fileName = getFileByLanguage(language);
        return YamlUtils.getValueForKey(key, fileName);
    }

    // add switch case to get file by language
    private static String getFileByLanguage(Language language) {
        return switch (language) {
            case EN -> "application_text_en.yaml";
            case ES -> "application_text_es.yaml";
            default -> "application_text.yaml";
        };
    }


    public enum Language {
        EN, ES
    }
}