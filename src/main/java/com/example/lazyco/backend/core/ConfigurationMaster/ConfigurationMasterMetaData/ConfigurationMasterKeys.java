package com.example.lazyco.backend.core.ConfigurationMaster.ConfigurationMasterMetaData;

import lombok.Getter;

@Getter
public enum ConfigurationMasterKeys {

  // AWS Configuration
  AWS_ACCESS_KEY("AWS_ACCESS_KEY"),
  AWS_SECRET_KEY("AWS_SECRET_KEY"),
  AWS_REGION("AWS_REGION"),
  AWS_S3_BUCKET("AWS_S3_BUCKET"),
  AWS_SES_FROM("AWS_SES_FROM"),

  // SMTP Configuration
  SMTP_USERNAME("SMTP_USERNAME"),
  SMTP_PASSWORD("SMTP_PASSWORD"),
  SMTP_HOST("SMTP_HOST"),
  SMTP_HOST_USER_ID("SMTP_HOST_USER_ID"),
  SMTP_HOST_PASSWORD("SMTP_HOST_PASSWORD"),
  SMTP_PORT("SMTP_PORT"),
  SMTP_SSL_PROTOCOL("SMTP_SSL_PROTOCOL"),
  SMTP_DEBUG("SMTP_DEBUG"),
  CLIENT_EMAIL("CLIENT_EMAIL"),

  // Other Configuration
  EMAIL_CLIENT("EMAIL_CLIENT"),
  ;
  private final String key;

  ConfigurationMasterKeys(String key) {
    this.key = key;
  }
}
