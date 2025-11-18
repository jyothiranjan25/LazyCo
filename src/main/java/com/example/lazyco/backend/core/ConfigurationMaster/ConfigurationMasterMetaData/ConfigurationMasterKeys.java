package com.example.lazyco.backend.core.ConfigurationMaster.ConfigurationMasterMetaData;

import lombok.Getter;

@Getter
public enum ConfigurationMasterKeys {
  // SMTP Configuration
  SMTP_USERNAME("SMTP_USERNAME"),
  SMTP_PASSWORD("SMTP_PASSWORD"),
  SMTP_HOST("SMTP_HOST"),
  SMTP_HOST_USER_ID("SMTP_HOST_USER_ID"),
  SMTP_HOST_PASSWORD("SMTP_HOST_PASSWORD"),
  SMTP_PORT("SMTP_PORT"),
  SMTP_SSL_PROTOCOL("SMTP_SSL_PROTOCOL"),
  SMTP_DEBUG("SMTP_DEBUG"),
  EMAIL_PROVIDER("EMAIL_PROVIDER"),
  ;
  private final String key;

  ConfigurationMasterKeys(String key) {
    this.key = key;
  }
}
