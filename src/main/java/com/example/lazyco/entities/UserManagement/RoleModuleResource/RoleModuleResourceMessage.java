package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum RoleModuleResourceMessage implements MessageCodes {
  ROLE_REQUIRED("ROLE_MODULE_RESOURCE_ROLE_REQUIRED"),
  MODULE_REQUIRED("ROLE_MODULE_RESOURCE_MODULE_REQUIRED"),
  RESOURCE_REQUIRED("ROLE_MODULE_RESOURCE_RESOURCE_REQUIRED");

  private final String value;

  RoleModuleResourceMessage(String value) {
    this.value = value;
  }
}
