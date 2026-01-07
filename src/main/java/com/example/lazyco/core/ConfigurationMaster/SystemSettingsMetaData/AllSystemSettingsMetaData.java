package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData;

import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.ApplicationSettings.ApplicationSettings;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.EmailSettings.EmailSettings;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.RolesSettings.RolesSettings;

public enum AllSystemSettingsMetaData implements SystemSettingsMetaData {
  EMAIL_SETTINGS("Email", "SMTP Account & Host Details", EmailSettings.class),
  APPLICATION_SETTINGS("Application", "URL & Mongo Database Settings", ApplicationSettings.class),
  ROLES_SETTINGS("Roles", "Admin & Security Permissions", RolesSettings.class),
  ;
  private final SystemSettingsMetaDataDTO metaData;

  AllSystemSettingsMetaData(
      String name, String description, Class<? extends SystemSettingsMetaData> groupEnumClass) {
    this.metaData = new SystemSettingsMetaDataDTO(name, description, groupEnumClass);
  }

  @Override
  public SystemSettingsMetaDataDTO getMetaData() {
    return metaData;
  }
}
