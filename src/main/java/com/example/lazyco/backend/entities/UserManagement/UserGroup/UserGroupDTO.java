package com.example.lazyco.backend.entities.UserManagement.UserGroup;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGroupDTO extends AbstractDTO<UserGroupDTO> {
  private String userGroupName;
  private String fullyQualifiedName;
}
