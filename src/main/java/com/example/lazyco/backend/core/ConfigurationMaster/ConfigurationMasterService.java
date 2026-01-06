package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.backend.core.Utils.Crypto.CryptoUtil;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationMasterService
    extends AbstractService<ConfigurationMasterDTO, ConfigurationMaster>
    implements IConfigurationMasterService {

  public ConfigurationMasterService(ConfigurationMasterMapper mapper) {
    super(mapper);
  }

  private final String MASKED_VALUE = "***********";

  @Override
  protected List<ConfigurationMasterDTO> modifyGetResult(
      List<ConfigurationMasterDTO> result, ConfigurationMasterDTO filter) {
    for (ConfigurationMasterDTO dto : result) {
      if (dto.getSensitiveConfigValue() != null) {
        dto.setSensitiveConfigValue(CryptoUtil.decrypt(dto.getSensitiveConfigValue()));
      }
    }
    return result;
  }

  @Override
  protected void preCreate(ConfigurationMasterDTO requestDTO, ConfigurationMaster entityToCreate) {
    if (Boolean.TRUE.equals(requestDTO.getSensitive())) {
      entityToCreate.setConfigValue(MASKED_VALUE);
      entityToCreate.setSensitiveConfigValue(CryptoUtil.encrypt(requestDTO.getConfigValue()));
    }
  }

  @Override
  protected void preUpdate(
      ConfigurationMasterDTO requestDTO,
      ConfigurationMaster entityBeforeUpdates,
      ConfigurationMaster entityAfterUpdates) {
    if (Boolean.TRUE.equals(requestDTO.getSensitive())
        && !requestDTO.getConfigValue().equals(MASKED_VALUE)) {
      entityAfterUpdates.setConfigValue(MASKED_VALUE);
      entityAfterUpdates.setSensitiveConfigValue(CryptoUtil.encrypt(requestDTO.getConfigValue()));
    }
  }

  public String getValue(String key) {
    ConfigurationMasterDTO filter = new ConfigurationMasterDTO();
    filter.setConfigKey(key);
    filter = getSingle(filter);
    if (filter == null) {
      return null;
    }
    return filter.getSensitive() ? filter.getSensitiveConfigValue() : filter.getConfigValue();
  }
}
