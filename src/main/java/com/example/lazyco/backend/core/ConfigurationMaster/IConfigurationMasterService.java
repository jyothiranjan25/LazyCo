package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;

public interface IConfigurationMasterService
    extends IAbstractService<ConfigurationMasterDTO, ConfigurationMaster> {
  String getValue(String string);
}
