package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService extends AbstractService<UserGroupDTO, UserGroup> {
  protected UserGroupService(UserGroupMapper UserGroupMapper) {
    super(UserGroupMapper);
  }
}
