package com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.RolesSettings;

import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;

public enum RolesSettings implements SystemSettingsMetaData {
  ROLE_ADMIN_SECURITY_SETTINGS(
      "ROLE SETTINGS",
      "Edit the functions and permissions assigned to different roles across Application.",
      RoleAdminSecuritySettingsMetaData.class);

  private final SystemSettingsMetaDataDTO metaData;

  RolesSettings(
      String name, String description, Class<? extends SystemSettingsMetaData> groupEnumClass) {
    this.metaData = new SystemSettingsMetaDataDTO(name, description, groupEnumClass);
  }

  @Override
  public SystemSettingsMetaDataDTO getMetaData() {
    return metaData;
  }
}
