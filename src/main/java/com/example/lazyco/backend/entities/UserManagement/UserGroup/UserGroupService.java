package com.example.lazyco.backend.entities.UserManagement.UserGroup;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService extends AbstractService<UserGroupDTO, UserGroup> {
  protected UserGroupService(UserGroupMapper UserGroupMapper) {
    super(UserGroupMapper);
  }
}
