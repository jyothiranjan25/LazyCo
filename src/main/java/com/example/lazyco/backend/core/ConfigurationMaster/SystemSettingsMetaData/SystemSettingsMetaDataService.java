package com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.Exceptions.ApplicationException;
import com.example.lazyco.backend.core.Exceptions.CommonMessage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsMetaDataService {

  private final AbstractAction abstractAction;

  public SystemSettingsMetaDataService(AbstractAction abstractAction) {
    this.abstractAction = abstractAction;
  }

  public SystemSettingsMetaDataDTO getSystemSettings() {
    // if system settings metadata is already cached, return it
    SystemSettingsMetaDataDTO allSystemSettings = SystemSettingsMetaDataCache.getSystemSettings();
    if (allSystemSettings != null) {
      return allSystemSettings;
    }

    // else form the system settings metadata, cache it and return it
    allSystemSettings = new SystemSettingsMetaDataDTO();
    allSystemSettings.setObjects(getMetaDataObjects(AllSystemSettingsMetaData.class));
    // cache the system settings metadata
    SystemSettingsMetaDataCache.setSystemSettings(allSystemSettings);
    return allSystemSettings;
  }

  // traverses through all the enum objects of the given enum class and returns their metadata list
  private List<SystemSettingsMetaDataDTO> getMetaDataObjects(
      Class<? extends SystemSettingsMetaData> groupEnumClass) {

    List<SystemSettingsMetaDataDTO> metaDataObjects = new ArrayList<>();
    for (SystemSettingsMetaData singleSetting : groupEnumClass.getEnumConstants()) {

      SystemSettingsMetaDataDTO singleSettingMetaData = singleSetting.getMetaData();
      // if this setting itself is a settings group,
      // then recursively get its children settings metadata objects
      if (singleSettingMetaData.getGroupEnumClass() != null) {
        singleSettingMetaData.setChildrenMetaData(
            getMetaDataObjects(singleSettingMetaData.getGroupEnumClass()));
      }
      metaDataObjects.add(singleSettingMetaData);
    }
    return metaDataObjects;
  }

  // fetches system setting value. if the value returned is null, returns ""
  public String getSystemSettingValueWithEmptyDefaultValue(
      SystemSettingsMetaData singleSystemSetting) {
    String configValue = getSystemSettingValue(singleSystemSetting);
    return (configValue != null) ? configValue : "";
  }

  // fetches the value of the received system setting from the database.
  // If not found, returns the default value associated with the system setting enum
  public String getSystemSettingValue(SystemSettingsMetaData singleSystemSetting) {
    if (singleSystemSetting.getMetaData().getConfigKey() == null) {
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    }
    if (abstractAction
        .getProperties()
        .containsKey(singleSystemSetting.getMetaData().getConfigKey())) {
      return abstractAction.getConfigProperties(singleSystemSetting.getMetaData().getConfigKey());
    } else {
      return singleSystemSetting.getMetaData().getDefaultValue();
    }
  }
}
