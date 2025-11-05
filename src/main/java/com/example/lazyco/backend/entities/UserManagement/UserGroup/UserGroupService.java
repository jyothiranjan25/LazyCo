package com.example.lazyco.backend.entities.UserManagement.UserGroup;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserGroupService extends AbstractService<UserGroupDTO, UserGroup> {
  protected UserGroupService(UserGroupMapper UserGroupMapper) {
    super(UserGroupMapper);
  }
}
