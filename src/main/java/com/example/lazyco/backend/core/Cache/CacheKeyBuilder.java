package com.example.lazyco.backend.core.Cache;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

public class CacheKeyBuilder {

  public static String of(String template, Object... values) {
    String result = template;
    int i = 0;

    while (result.contains("{}") && i < values.length) {
      result = result.replaceFirst("\\{}", String.valueOf(values[i++]));
    }

    return result;
  }

  public static String fromDto(String prefix, Object dto) {
    if (dto == null) {
      return prefix;
    }

    StringBuilder key = new StringBuilder(prefix);

    Arrays.stream(dto.getClass().getDeclaredFields())
        .sorted(Comparator.comparing(Field::getName)) // deterministic order
        .forEach(
            field -> {
              field.setAccessible(true);
              try {
                Object value = field.get(dto);
                if (value != null) {
                  key.append(":").append(field.getName()).append("=").append(value);
                }
              } catch (IllegalAccessException ignored) {
              }
            });
    return key.toString();
  }
}
