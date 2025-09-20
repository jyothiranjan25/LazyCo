package com.example.lazyco.backend.core.CsvTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvObjectMapper {

  private static final ObjectMapper mapper =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
          .setPropertyNamingStrategy(new CsvFieldNamingStrategy());

  public static List csvMapper(List<?> objects) {
    return objects.stream()
        .map(obj -> mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {}))
        .collect(Collectors.toList());
  }

  private static class CsvFieldNamingStrategy extends PropertyNamingStrategies.NamingBase {
    @Override
    public String translate(String s) {
      return CsvStrategies.fieldNamingStrategy(s);
    }
  }
}
