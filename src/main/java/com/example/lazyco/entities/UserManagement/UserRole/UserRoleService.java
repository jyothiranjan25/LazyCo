package com.example.lazyco.backend.entities.UserManagement.UserRole;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends AbstractService<UserRoleDTO, UserRole> {
  protected UserRoleService(UserRoleMapper userRoleMapper) {
    super(userRoleMapper);
  }
}
