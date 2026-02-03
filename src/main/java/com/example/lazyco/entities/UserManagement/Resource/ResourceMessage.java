package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ResourceMessage implements MessageCodes {
  RESOURCE_NAME_REQUIRED("RESOURCE.RESOURCE_NAME_REQUIRED"),
  DUPLICATE_RESOURCE_NAME("RESOURCE.DUPLICATE_RESOURCE_NAME"),
  ;
  private final String value;

  ResourceMessage(String value) {
    this.value = value;
  }
}
