package com.example.lazyco.backend.entities.UserManagement.Role;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService extends AbstractService<RoleDTO, Role> {
  protected RoleService(RoleMapper roleMapper) {
    super(roleMapper);
  }
}
