package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CommonMessage implements MessageCodes {
  CUSTOM_MESSAGE("COMMON_MODULES.CUSTOM_MESSAGE"),
  APPLICATION_ERROR("COMMON_MODULES.APPLICATION_ERROR"),
  ATOMIC_OPERATION_ERROR("COMMON_MODULES.ATOMIC_OPERATION_ERROR"),
  OBJECT_REQUIRED("COMMON_MODULES.OBJECT_REQUIRED"),
  OBJECT_NOT_FOUND("COMMON_MODULES.OBJECT_NOT_FOUND"),
  ID_REQUIRED("COMMON_MODULES.ID_REQUIRED"),
  ;
  private final String value;

  CommonMessage(String value) {
    this.value = value;
  }
}
