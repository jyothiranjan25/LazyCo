package com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.EmailSettings;

import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;

// enum containing all the email settings
public enum EmailSettings implements SystemSettingsMetaData {
  SMTP_ACCOUNT_DETAILS(
      "SMTP ACCOUNT DETAILS",
      "Your SMTP username is your email account and the SMTP password is the password for your email that you are using to sign in.",
      SMTPAccountSettingsMetaData.class),
  SMTP_HOST_SETTINGS(
      "SMTP HOST",
      "The SMTP host forwards your message to a backup server.",
      SMTPHostSettingsMetaData.class),
  SMTP_NETWORK_SETTINGS(
      "SMTP NETWORK",
      "Configure the delivery endpoints, delivery method and from address for sending emails.",
      SMTPNetworkSettingsMetaData.class);

  private final SystemSettingsMetaDataDTO metaData;

  EmailSettings(
      String name, String description, Class<? extends SystemSettingsMetaData> groupEnumClass) {
    this.metaData = new SystemSettingsMetaDataDTO(name, description, groupEnumClass);
  }

  @Override
  public SystemSettingsMetaDataDTO getMetaData() {
    return metaData;
  }
}
