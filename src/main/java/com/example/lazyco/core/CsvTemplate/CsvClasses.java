package com.example.lazyco.core.CsvTemplate;

import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum CsvClasses {
  APP_USER_DTO("APP_USER", AppUserDTO.class),
  ;
  private final String key;
  private final Class<?> csvDTOClass;

  CsvClasses(String key, Class<?> csvDTOClass) {
    this.key = key;
    this.csvDTOClass = csvDTOClass;
  }

  private static final Map<String, CsvClasses> CSV_MAP = new HashMap<>();

  static {
    for (CsvClasses csvClasses : CsvClasses.values()) {
      CSV_MAP.put(csvClasses.getKey(), csvClasses);
    }
  }

  public static CsvClasses getByKey(String key) {
    return CSV_MAP.get(key);
  }
}
