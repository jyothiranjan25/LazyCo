package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData;

public class SystemSettingsMetaDataCache {
  private static SystemSettingsMetaDataDTO allSystemSettings = null;

  public static SystemSettingsMetaDataDTO getSystemSettings() {
    return allSystemSettings;
  }

  public static void setSystemSettings(SystemSettingsMetaDataDTO systemSettings) {
    allSystemSettings = systemSettings;
  }
}
