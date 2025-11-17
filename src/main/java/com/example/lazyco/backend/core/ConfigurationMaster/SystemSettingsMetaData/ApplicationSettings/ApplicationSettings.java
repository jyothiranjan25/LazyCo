package com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.ApplicationSettings;

import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;

public enum ApplicationSettings implements SystemSettingsMetaData {
  URL_SETTINGS(
      "URL",
      "This is the site URL for the application currently being used.",
      URLSettingsMetaData.class),
  ENVIRONMENT_SETTINGS("ENVIRONMENT", "Environment Settings", EnvironmentSettingsMetaData.class),
  MONGO_DATABASE_SETTINGS(
      "MONGO DATABASE", "Mongo Database Settings", MongoDatabaseSettingsMetaData.class),
  REDIS_CACHE_SETTINGS("REDIS CACHE", "Redis Cache Settings", RedisCacheSettingsMetaData.class),
  ;
  private final SystemSettingsMetaDataDTO metaData;

  ApplicationSettings(
      String name, String description, Class<? extends SystemSettingsMetaData> groupEnumClass) {
    this.metaData = new SystemSettingsMetaDataDTO(name, description, groupEnumClass);
  }

  @Override
  public SystemSettingsMetaDataDTO getMetaData() {
    return metaData;
  }
}
