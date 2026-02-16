package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.EmailSettings;

import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.core.Utils.FieldTypeEnum;

public enum SMTPHostSettingsMetaData implements SystemSettingsMetaData {
  SMTP_HOST(
      "SMTP Host",
      "SMTP Host is the server that will send out your email.",
      SystemSettingsKeys.SMTP_HOST.getValue(),
      FieldTypeEnum.TEXT,
      "smtp.example.com"),
  SMTP_HOST_USER_ID(
      "SMTP Host User ID",
      "SMTP Host User ID is the user name used to sign in to the smtp server.",
      SystemSettingsKeys.SMTP_HOST_USER_ID.getValue(),
      FieldTypeEnum.TEXT,
      "username@example.com"),
  SMTP_HOST_PASSWORD(
      "SMTP Host Password",
      "Your password must have 8 characters and meet the following criteria: 1 Capital Letter | 1 Small Letter | 1 Number | 1 Symbol",
      SystemSettingsKeys.SMTP_HOST_PASSWORD.getValue(),
      FieldTypeEnum.TEXT,
      "SMTP host password");

  private final SystemSettingsMetaDataDTO metaData;

  SMTPHostSettingsMetaData(
      String name,
      String description,
      String configKey,
      FieldTypeEnum inputType,
      String placeholder) {
    this.metaData =
        new SystemSettingsMetaDataDTO(name, description, configKey, inputType, placeholder, null);
  }

  @Override
  public SystemSettingsMetaDataDTO getMetaData() {
    return metaData;
  }
}
