package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.ConfigurationMaster.ConfigurationMasterMetaData.ConfigurationMasterMetaDataDTO;
import com.example.lazyco.backend.core.ConfigurationMaster.ConfigurationMasterMetaData.ConfigurationMasterMetaDataService;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataService;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuration_master")
public class ConfigurationMasterController extends AbstractController<ConfigurationMasterDTO> {

  private final ConfigurationMasterMetaDataService configurationMasterMetaDataService;
  private final SystemSettingsMetaDataService systemSettingsMetaDataService;

  public ConfigurationMasterController(
      IConfigurationMasterService configurationMasterService,
      ConfigurationMasterMetaDataService configurationMasterMetaDataService,
      SystemSettingsMetaDataService systemSettingsMetaDataService) {
    super(configurationMasterService);
    this.configurationMasterMetaDataService = configurationMasterMetaDataService;
    this.systemSettingsMetaDataService = systemSettingsMetaDataService;
  }

  @GetMapping("/get_configuration_master_metadata")
  public ResponseEntity<ConfigurationMasterMetaDataDTO> getConfigurationMasterMetadata() {
    ConfigurationMasterMetaDataDTO metaData =
        configurationMasterMetaDataService.getConfigurationMaster();
    return ResponseUtils.sendResponse(metaData);
  }

  @GetMapping("/get_system_settings_metadata")
  public ResponseEntity<SystemSettingsMetaDataDTO> getSystemSettingsMetaData() {
    SystemSettingsMetaDataDTO systemSettings = systemSettingsMetaDataService.getSystemSettings();
    return ResponseUtils.sendResponse(systemSettings);
  }
}
