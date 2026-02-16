package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.ApplicationSettings;

import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.core.Utils.FieldTypeEnum;

public enum MongoDatabaseSettingsMetaData implements SystemSettingsMetaData {
  MONGO_DB_NAME(
      "Mongo DB Name",
      "The name that shows up on your Mongo Database.",
      SystemSettingsKeys.MONGO_DB_NAME.getValue(),
      FieldTypeEnum.TEXT,
      "Database name"),
  MONGO_DB_URL(
      "Mongo DB URL",
      "Enter the connection string for the local server of the database you want to connect to.",
      SystemSettingsKeys.MONGO_DB_URL.getValue(),
      FieldTypeEnum.TEXT,
      "mongodb://127.0.0.1:27017");

  private final SystemSettingsMetaDataDTO metaData;

  MongoDatabaseSettingsMetaData(
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
