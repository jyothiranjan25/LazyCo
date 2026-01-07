package com.example.lazyco.core.ConfigurationMaster.ConfigurationMasterMetaData;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Utils.FieldInputType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurationMasterMetaDataDTO extends AbstractDTO<ConfigurationMasterMetaDataDTO> {

  private String name;
  private String description;
  private String configKey;
  private FieldInputType inputType;
  private Boolean isSensitive;
  private String placeholder;
  private String defaultValue;
  private String[] options;

  public ConfigurationMasterMetaDataDTO(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      Boolean isSensitive,
      String placeholder,
      String defaultValue,
      String[] options) {
    this(name, description, configKey, inputType, isSensitive, placeholder, options);
    this.defaultValue = defaultValue;
  }

  public ConfigurationMasterMetaDataDTO(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      Boolean isSensitive,
      String placeholder,
      String[] options) {
    this.name = name;
    this.description = description;
    this.configKey = configKey;
    this.inputType = inputType;
    this.isSensitive = isSensitive;
    this.placeholder = placeholder;
    this.options = options;
  }
}
