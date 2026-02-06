package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class RoleModuleResourceService
    extends AbstractService<RoleModuleResourceDTO, RoleModuleResource> {
  protected RoleModuleResourceService(RoleModuleResourceMapper roleModuleResourceMapper) {
    super(roleModuleResourceMapper);
  }

  @Override
  public RoleModuleResourceDTO executeCreateTransactional(RoleModuleResourceDTO dto) {
    RoleModuleResourceDTO filter = new RoleModuleResourceDTO();
    filter.setRoleId(dto.getRoleId());
    filter.setModuleId(dto.getModuleId());
    filter.setResourceId(dto.getResourceId());

    if (getCount(filter) < 1) {
      return super.executeCreateTransactional(dto);
    }
    return dto;
  }
}
