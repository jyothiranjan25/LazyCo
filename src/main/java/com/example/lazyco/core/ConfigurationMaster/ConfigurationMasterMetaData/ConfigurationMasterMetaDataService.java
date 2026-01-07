package com.example.lazyco.core.ConfigurationMaster.ConfigurationMasterMetaData;

import com.example.lazyco.core.ConfigurationMaster.ConfigurationMasterService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationMasterMetaDataService {

  private final ConfigurationMasterService configurationMasterService;

  public ConfigurationMasterMetaDataService(ConfigurationMasterService configurationMasterService) {
    this.configurationMasterService = configurationMasterService;
  }

  public ConfigurationMasterMetaDataDTO getConfigurationMaster() {
    // if Configuration Master metadata is already cached, return it
    ConfigurationMasterMetaDataDTO allCongigurationMaster =
        ConfigurationMasterMetaDataCache.getConfigurationMaster();

    if (allCongigurationMaster != null) {
      return allCongigurationMaster;
    }

    // else form the Configuration Master metadata, cache it and return it
    allCongigurationMaster = new ConfigurationMasterMetaDataDTO();
    allCongigurationMaster.setObjects(getMetaDataObjects(AllConfigurationMasterMetaData.class));
    // cache the system settings metadata
    ConfigurationMasterMetaDataCache.setConfigurationMaster(allCongigurationMaster);
    return allCongigurationMaster;
  }

  // traverses through all the enum objects of the given enum class and returns their metadata list
  private List<ConfigurationMasterMetaDataDTO> getMetaDataObjects(
      Class<? extends ConfigurationMasterMetaData> groupEnumClass) {

    List<ConfigurationMasterMetaDataDTO> metaDataObjects = new ArrayList<>();
    for (ConfigurationMasterMetaData singleConfiguration : groupEnumClass.getEnumConstants()) {

      ConfigurationMasterMetaDataDTO singleConfigurationMetaData =
          (ConfigurationMasterMetaDataDTO) singleConfiguration.getMetaData().clone();
      metaDataObjects.add(singleConfigurationMetaData);
    }
    return metaDataObjects;
  }

  public String getConfigurationMasterValueWithEmptyDefaultValue(
      ConfigurationMasterMetaData singleConfiguration) {
    String configValue = getConfigurationMasterValue(singleConfiguration);
    return (configValue != null) ? configValue : "";
  }

  public String getConfigurationMasterValue(ConfigurationMasterMetaData singleConfiguration) {
    if (singleConfiguration.getMetaData().getConfigKey() == null) {
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    }

    String configKey = singleConfiguration.getMetaData().getConfigKey();
    String configValue = configurationMasterService.getValue(configKey);

    if (!StringUtils.isEmpty(configValue)) {
      return configValue;
    } else {
      return singleConfiguration.getMetaData().getDefaultValue();
    }
  }
}
