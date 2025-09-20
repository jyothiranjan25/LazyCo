package com.example.lazyco.backend.core.CsvTemplate;

import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvField;
import java.lang.reflect.Field;
import org.apache.commons.lang3.StringUtils;

public class CsvStrategies {

  public static String fieldNamingStrategy(Field field) {
    CsvField csv = field.getAnnotation(CsvField.class);
    if (csv != null && StringUtils.isNotBlank(csv.name())) {
      // Use the explicit name from @CsvField
      return csv.name();
    }
    String javaName = field.getName();
    return fieldNamingStrategy(javaName);
  }

  public static String fieldNamingStrategy(String fieldName) {
    String withSpaces =
        fieldName
            .replaceAll("([a-z])([A-Z])", "$1 $2") // insert space before capital
            .replaceAll("_+", " "); // also turn underscores into spaces

    // Capitalize first character for a nice header
    return StringUtils.capitalize(withSpaces);
  }
}
