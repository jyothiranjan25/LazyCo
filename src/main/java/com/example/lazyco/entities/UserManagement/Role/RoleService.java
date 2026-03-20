package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.entities.UserManagement.RoleModuleResource.RoleModuleResourceDTO;
import com.example.lazyco.entities.UserManagement.RoleModuleResource.RoleModuleResourceService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends CommonAbstractService<RoleDTO, Role> {

  private final RoleModuleResourceService roleModuleResourceService;

  protected RoleService(
      RoleMapper roleMapper, RoleModuleResourceService roleModuleResourceService) {
    super(roleMapper);
    this.roleModuleResourceService = roleModuleResourceService;
  }

  @Override
  protected List<RoleDTO> modifyGetResult(List<RoleDTO> result, RoleDTO filter) {
    for (RoleDTO roleDTO : result) {
      if (roleDTO.getRoleModuleResources() != null && !roleDTO.getRoleModuleResources().isEmpty()) {
        roleDTO.setRoleModuleResources(
            roleModuleResourceService.mapModuleResourceTree(roleDTO.getRoleModuleResources()));
      }
    }
    return result;
  }

  @Override
  protected void validateBeforeCreate(RoleDTO request) {
    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(RoleMessage.ROLE_NAME_REQUIRED);
    }

    // name should be unique
    validateUniqueName(request, RoleMessage.DUPLICATE_ROLE_NAME);
  }

  @Override
  protected void postCreate(RoleDTO request, Role createdEntity, RoleDTO createdDTO) {
    mapRoleModuleResources(request, createdEntity);
  }

  @Override
  protected void validateBeforeUpdate(RoleDTO request) {
    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, RoleMessage.DUPLICATE_ROLE_NAME);
    }
  }

  @Override
  protected void postUpdate(RoleDTO request, RoleDTO entityBeforeUpdate, Role updatedEntity) {
    mapRoleModuleResources(request, updatedEntity);
  }

  public void mapRoleModuleResources(RoleDTO request, Role entity) {
    if (request.getAddRoleModuleResources() != null
        && !request.getAddRoleModuleResources().isEmpty()) {
      for (RoleModuleResourceDTO roleModuleResourceDTO : request.getAddRoleModuleResources()) {
        roleModuleResourceDTO.setRoleId(entity.getId());
        roleModuleResourceService.executeCreateTransactional(roleModuleResourceDTO);
      }
    }
    if (request.getUpdateRoleModuleResources() != null
        && !request.getUpdateRoleModuleResources().isEmpty()) {
      for (RoleModuleResourceDTO roleModuleResourceDTO : request.getUpdateRoleModuleResources()) {
        roleModuleResourceDTO.setRoleId(entity.getId());
        roleModuleResourceService.directUpdate(roleModuleResourceDTO);
      }
    }
    if (request.getDeleteRoleModuleResources() != null
        && !request.getDeleteRoleModuleResources().isEmpty()) {
      for (RoleModuleResourceDTO roleModuleResourceDTO : request.getDeleteRoleModuleResources()) {
        roleModuleResourceDTO.setRoleId(entity.getId());
        roleModuleResourceService.directDelete(roleModuleResourceDTO);
      }
    }
  }
}
