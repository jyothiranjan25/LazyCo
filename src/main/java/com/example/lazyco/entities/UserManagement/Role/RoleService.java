package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<RoleDTO, Role> {
  protected RoleService(RoleMapper roleMapper) {
    super(roleMapper);
  }

  @Override
  protected void validateBeforeCreate(RoleDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getRoleName())) {
      throw new ApplicationException(RoleMessage.ROLE_NAME_REQUIRED);
    }
    validateDuplicateName(requestDTO.getRoleName(), null);
  }

  @Override
  protected void validateBeforeUpdate(RoleDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getRoleName())) {
      validateDuplicateName(requestDTO.getRoleName(), requestDTO.getId());
    }
  }

  private void validateDuplicateName(String roleName, Long excludeId) {
    RoleDTO filter = new RoleDTO();
    filter.setRoleName(roleName);
    if (excludeId != null) {
      filter.setIdsNotIn(List.of(excludeId));
    }
    if (getCount(filter) > 0) {
      throw new ApplicationException(RoleMessage.DUPLICATE_ROLE_NAME, new Object[] {roleName});
    }
  }
}
