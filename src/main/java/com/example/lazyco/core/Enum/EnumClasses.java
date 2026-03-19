package com.example.lazyco.core.Enum;

import com.example.lazyco.core.Utils.FieldTypeEnum;
import com.example.lazyco.entities.AcademicProgram.ProgramLevelEnum;
import com.example.lazyco.entities.AcademicProgram.ProgramStudyModeEnum;
import java.util.HashMap;
import java.util.Map;

import com.example.lazyco.entities.UserManagement.Role.RoleTypeEnum;
import lombok.Getter;

@Getter
public enum EnumClasses {
  FIELD_INPUT_TYPE("FIELD_INPUT_TYPE", FieldTypeEnum.class),
  ROLE_TYPE_ENUM("ROLE_TYPE_ENUM", RoleTypeEnum.class),
  PROGRAM_LEVEL_ENUM("PROGRAM_LEVEL_ENUM", ProgramLevelEnum.class),
  PROGRAM_STUDY_MODE_ENUM("PROGRAM_STUDY_MODE_ENUM", ProgramStudyModeEnum.class);
  private final String key;
  private final Class<?> enumClass;

  EnumClasses(String key, Class<?> enumClass) {
    this.key = key;
    this.enumClass = enumClass;
  }

  private static final Map<String, EnumClasses> ENUM_MAP = new HashMap<>();

  static {
    for (EnumClasses enumClass : EnumClasses.values()) {
      ENUM_MAP.put(enumClass.getKey(), enumClass);
    }
  }

  public static EnumClasses getByKey(String key) {
    return ENUM_MAP.get(key);
  }
}
