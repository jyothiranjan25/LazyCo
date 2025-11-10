package com.example.lazyco.backend.entities.UserManagement.UserRole;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRoleService extends AbstractService<UserRoleDTO, UserRole> {
  protected UserRoleService(UserRoleMapper userRoleMapper) {
    super(userRoleMapper);
  }
}
