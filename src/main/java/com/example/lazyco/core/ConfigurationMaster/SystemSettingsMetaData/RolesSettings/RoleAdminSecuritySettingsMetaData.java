package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.RolesSettings;

import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.core.Utils.FieldTypeEnum;

public enum RoleAdminSecuritySettingsMetaData implements SystemSettingsMetaData {
  ROLE_ADMIN(
      "Role Admin",
      "Select who will have Administrator access.",
      SystemSettingsKeys.ROLE_ADMIN.getValue(),
      FieldTypeEnum.TEXT,
      "Admin"),
  SECURITY_ROLE(
      "Security Role",
      "Select who will have Security access.",
      SystemSettingsKeys.SECURITY_ROLE.getValue(),
      FieldTypeEnum.TEXT,
      "Super Admin"),
  SECURITY_ROLE_CLIENT(
      "Security Role Client",
      "Select who will have Security access from the client end.",
      SystemSettingsKeys.SECURITY_ROLE_CLIENT.getValue(),
      FieldTypeEnum.TEXT,
      "Super Admin Client");

  private final SystemSettingsMetaDataDTO metaData;

  RoleAdminSecuritySettingsMetaData(
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
