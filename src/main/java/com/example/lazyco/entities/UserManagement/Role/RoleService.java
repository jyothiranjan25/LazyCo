package com.example.lazyco.backend.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<RoleDTO, Role> {
  protected RoleService(RoleMapper roleMapper) {
    super(roleMapper);
  }
}
