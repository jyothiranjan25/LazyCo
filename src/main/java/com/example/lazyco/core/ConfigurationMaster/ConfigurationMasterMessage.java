package com.example.lazyco.core.ConfigurationMaster;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ConfigurationMasterMessage implements MessageCodes {
  ID_NOT_FOUND("CONFIGURATION_MASTER.ID_NOT_FOUND"),
  DUPLICATE_KEY("CONFIGURATION_MASTER.DUPLICATE_KEY"),

  OBJECT_NOT_FOUND("CONFIGURATION_MASTER.OBJECT_NOT_FOUND");
  final String value;

  ConfigurationMasterMessage(String value) {
    this.value = value;
  }
}
