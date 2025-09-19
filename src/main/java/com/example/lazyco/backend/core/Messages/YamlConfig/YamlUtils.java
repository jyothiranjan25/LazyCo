package com.example.lazyco.backend.core.Messages.YamlConfig;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.yaml.snakeyaml.Yaml;

public class YamlUtils {

  private static final Map<String, Map<String, Object>> cache = new ConcurrentHashMap<>();

  public static String getValueForKey(String key, String filePath) {
    Map<String, Object> yamlMap =
        cache.computeIfAbsent(
            filePath,
            fp -> {
              try (InputStream inputStream =
                  YamlUtils.class.getClassLoader().getResourceAsStream(fp)) {
                if (inputStream == null) {
                  ApplicationLogger.error("YAML file not found: " + fp, YamlUtils.class);
                  return Map.of();
                }
                return new Yaml().load(inputStream);
              } catch (Exception e) {
                ApplicationLogger.error(e, YamlUtils.class);
                return Map.of();
              }
            });

    if (yamlMap == null || yamlMap.isEmpty()) {
      return key; // fallback
    }

    return getValue(yamlMap, key);
  }

  public static String getValueForKey(
      String key, String filePath, ApplicationText.Language language) {
    try (InputStream inputStream = YamlUtils.class.getClassLoader().getResourceAsStream(filePath)) {
      Map<String, Object> yamlMap = new Yaml().load(inputStream);
      if (yamlMap == null || yamlMap.isEmpty()) {
        return key; // fallback
      }
      return getValue(yamlMap, key);
    } catch (IOException e) {
      ApplicationLogger.error(e, e.getClass());
      return key;
    }
  }

  @SuppressWarnings({"unchecked"})
  public static String getValue(Map<String, Object> yamlMap, String key) {
    String[] keys = key.split("\\.");
    Map<String, Object> currentMap = yamlMap;

    for (int i = 0; i < keys.length - 1; i++) {
      Object value = currentMap.get(keys[i]);
      if (value instanceof Map) {
        currentMap = (Map<String, Object>) value;
      } else {
        return key;
      }
    }

    Object finalValue = currentMap.get(keys[keys.length - 1]);
    return (finalValue instanceof String) ? (String) finalValue : key;
  }
}
