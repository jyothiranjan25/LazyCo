package com.example.lazyco.backend.core.ConfigurationMaster.ConfigurationMasterMetaData;

import com.example.lazyco.backend.core.Utils.FieldInputType;

public enum AllConfigurationMasterMetaData implements ConfigurationMasterMetaData {
  SMTP_USERNAME(
      "SMTP Username",
      "The username used to authenticate the SMTP server.",
      ConfigurationMasterKeys.SMTP_USERNAME.getKey(),
      FieldInputType.TEXT,
      false,
      "Enter SMTP Username"),

  SMTP_PASSWORD(
      "SMTP Password",
      "The password used to authenticate the SMTP server.",
      ConfigurationMasterKeys.SMTP_PASSWORD.getKey(),
      FieldInputType.TEXT,
      true,
      "Enter SMTP Password"),

  SMTP_HOST(
      "SMTP Host",
      "The SMTP Host used to send emails.",
      ConfigurationMasterKeys.SMTP_HOST.getKey(),
      FieldInputType.TEXT,
      false,
      "Enter SMTP Host"),

  SMTP_HOST_USER_ID(
      "SMTP Host User ID",
      "The user ID used to authenticate the SMTP server.",
      ConfigurationMasterKeys.SMTP_HOST_USER_ID.getKey(),
      FieldInputType.TEXT,
      false,
      "Enter SMTP Host User ID"),

  SMTP_HOST_PASSWORD(
      "SMTP Host Password",
      "The password used to authenticate the SMTP server.",
      ConfigurationMasterKeys.SMTP_HOST_PASSWORD.getKey(),
      FieldInputType.TEXT,
      true,
      "Enter SMTP Host Password"),

  SMTP_SSL_PROTOCOL(
      "SMTP SSL Protocol",
      "The SSL protocol used to connect to the SMTP server.",
      ConfigurationMasterKeys.SMTP_SSL_PROTOCOL.getKey(),
      FieldInputType.SELECT,
      false,
      "Enter SMTP SSL Protocol",
      new String[] {"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"}),

  SMTP_PORT(
      "SMTP Port",
      "The port used to connect to the SMTP server.",
      ConfigurationMasterKeys.SMTP_PORT.getKey(),
      FieldInputType.NUMBER,
      false,
      "Enter SMTP Port"),

  SMTP_DEBUG(
      "SMTP Debug",
      "The debug mode used to connect to the SMTP server.",
      ConfigurationMasterKeys.SMTP_DEBUG.getKey(),
      FieldInputType.SELECT,
      false,
      "Enter SMTP Debug",
      "TRUE",
      new String[] {"TRUE", "FALSE"}),

  EMAIL_PROVIDER(
      "SMTP Client",
      "The email client used to send emails.",
      ConfigurationMasterKeys.EMAIL_PROVIDER.getKey(),
      FieldInputType.SELECT,
      false,
      "",
      "SMTP",
      new String[] {"SMTP", "AWS"}),
  ;
  private final ConfigurationMasterMetaDataDTO metaData;

  AllConfigurationMasterMetaData(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      boolean isSensitive,
      String placeholder) {
    this.metaData =
        new ConfigurationMasterMetaDataDTO(
            name, description, configKey, inputType, isSensitive, placeholder, null);
  }

  AllConfigurationMasterMetaData(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      boolean isSensitive,
      String placeholder,
      String[] options) {
    this.metaData =
        new ConfigurationMasterMetaDataDTO(
            name, description, configKey, inputType, isSensitive, placeholder, options);
  }

  AllConfigurationMasterMetaData(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      boolean isSensitive,
      String placeholder,
      String defaultValue,
      String[] options) {
    this.metaData =
        new ConfigurationMasterMetaDataDTO(
            name,
            description,
            configKey,
            inputType,
            isSensitive,
            placeholder,
            defaultValue,
            options);
  }

  @Override
  public ConfigurationMasterMetaDataDTO getMetaData() {
    return metaData;
  }
}
