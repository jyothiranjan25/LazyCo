package com.example.lazyco.core.ConfigurationMaster;

import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;

public interface IConfigurationMasterService
    extends IAbstractService<ConfigurationMasterDTO, ConfigurationMaster> {
  String getValue(String string);
}
