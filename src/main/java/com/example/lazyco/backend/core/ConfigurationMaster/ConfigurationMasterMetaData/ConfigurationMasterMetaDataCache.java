package com.example.lazyco.backend.core.ConfigurationMaster.ConfigurationMasterMetaData;

public class ConfigurationMasterMetaDataCache {
  private static ConfigurationMasterMetaDataDTO allConfigurationMaster = null;

  public static ConfigurationMasterMetaDataDTO getConfigurationMaster() {
    return allConfigurationMaster;
  }

  public static void setConfigurationMaster(
      ConfigurationMasterMetaDataDTO configurationMasterMetaDataDTO) {
    allConfigurationMaster = configurationMasterMetaDataDTO;
  }
}
