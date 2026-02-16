package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Utils.FieldTypeEnum;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SystemSettingsMetaDataDTO extends AbstractDTO<SystemSettingsMetaDataDTO> {
  private String name;
  private String description;
  private String configKey;
  private FieldTypeEnum inputType;
  private String placeholder;
  private String defaultValue;
  private String[] options;
  private transient Class<? extends SystemSettingsMetaData> groupEnumClass;

  private List<SystemSettingsMetaDataDTO> childrenMetaData;

  public SystemSettingsMetaDataDTO(
      String name,
      String description,
      String configKey,
      FieldTypeEnum inputType,
      String placeholder,
      String defaultValue,
      String[] options) {
    this(name, description, configKey, inputType, placeholder, options);
    this.defaultValue = defaultValue;
  }

  public SystemSettingsMetaDataDTO(
      String name,
      String description,
      String configKey,
      FieldTypeEnum inputType,
      String placeholder,
      String[] options) {
    this.name = name;
    this.description = description;
    this.configKey = configKey;
    this.inputType = inputType;
    this.placeholder = placeholder;
    this.options = options;
  }

  public SystemSettingsMetaDataDTO(
      String name, String description, Class<? extends SystemSettingsMetaData> groupEnumClass) {
    this.name = name;
    this.description = description;
    this.groupEnumClass = groupEnumClass;
  }
}
