package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class RoleModuleResourceService
    extends AbstractService<RoleModuleResourceDTO, RoleModuleResource> {
  protected RoleModuleResourceService(RoleModuleResourceMapper roleModuleResourceMapper) {
    super(roleModuleResourceMapper);
  }
}
