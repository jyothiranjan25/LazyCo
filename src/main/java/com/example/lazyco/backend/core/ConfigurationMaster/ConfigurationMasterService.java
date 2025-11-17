package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfigurationMasterService
    extends AbstractService<ConfigurationMasterDTO, ConfigurationMaster>
    implements IConfigurationMasterService {

  public ConfigurationMasterService(ConfigurationMasterMapper mapper) {
    super(mapper);
  }

  public String getValue(String key) {
    ConfigurationMasterDTO filter = new ConfigurationMasterDTO();
    filter.setConfigKey(key);
    filter = getSingle(filter);
    if (filter == null) {
      return null;
    }
    return filter.getConfigValue();
  }
}
