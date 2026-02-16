package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.EmailSettings;

import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.core.Utils.FieldTypeEnum;

public enum SMTPAccountSettingsMetaData implements SystemSettingsMetaData {
  SMTP_EMAIL_CLIENT(
      "SMTP Email Client",
      "Your SMTP email client is the email address that you will use to send emails from.",
      SystemSettingsKeys.SMTP_EMAIL_CLIENT.getValue(),
      FieldTypeEnum.TEXT,
      "username@example.com"),
  SMTP_USERNAME(
      "SMTP Username",
      "Your SMTP username is your email address that you can use to sign in to your account.",
      SystemSettingsKeys.SMTP_USERNAME.getValue(),
      FieldTypeEnum.TEXT,
      "username@example.com"),
  SMTP_PASSWORD(
      "SMTP Password",
      "Your SMTP password is the password for your email account that you will use to sign in.",
      SystemSettingsKeys.SMTP_PASSWORD.getValue(),
      FieldTypeEnum.TEXT,
      "SMTP password");

  private final SystemSettingsMetaDataDTO metaData;

  SMTPAccountSettingsMetaData(
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
