package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.EmailSettings;

import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.core.Utils.FieldTypeEnum;

public enum SMTPNetworkSettingsMetaData implements SystemSettingsMetaData {
  SMTP_SSL_PROTOCOL(
      "SMTP SSL Protocol",
      "Select the SMTP encryption protocol to keep data secure when being transferred over a network",
      SystemSettingsKeys.SMTP_SSL_PROTOCOL.getValue(),
      FieldTypeEnum.SELECT,
      "TLSv1.2",
      new String[] {"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"}),

  SMTP_PORT(
      "SMTP Port",
      "An SMTP port is a communication endpoint that is designed to direct email through a network, from one server to another, to its recipient.",
      SystemSettingsKeys.SMTP_PORT.getValue(),
      FieldTypeEnum.NUMBER,
      "587",
      null),

  SMTP_DEBUG(
      "SMTP Debug",
      "Enable this for detailed logging of the SMTP communication process.",
      SystemSettingsKeys.SMTP_DEBUG.getValue(),
      FieldTypeEnum.SELECT,
      "SELECT",
      new String[] {"TRUE", "FALSE"});

  private final SystemSettingsMetaDataDTO metaData;

  SMTPNetworkSettingsMetaData(
      String name,
      String description,
      String configKey,
      FieldTypeEnum inputType,
      String placeholder,
      String[] options) {
    this.metaData =
        new SystemSettingsMetaDataDTO(
            name, description, configKey, inputType, placeholder, options);
  }

  @Override
  public SystemSettingsMetaDataDTO getMetaData() {
    return metaData;
  }
}
