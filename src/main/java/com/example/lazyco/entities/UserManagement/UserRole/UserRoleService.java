package com.example.lazyco.entities.UserManagement.UserRole;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends AbstractService<UserRoleDTO, UserRole> {
  protected UserRoleService(UserRoleMapper userRoleMapper) {
    super(userRoleMapper);
  }

  @Override
  protected void validateBeforeCreate(UserRoleDTO request) {
    if (request.getRoleId() == null) {
      throw new ApplicationException(UserRoleMessage.ROLE_ID_REQUIRED);
    }
    if (request.getAppUserId() == null) {
      throw new ApplicationException(UserRoleMessage.APP_USER_ID_REQUIRED);
    }
    if (request.getUserGroupId() == null) {
      throw new ApplicationException(UserRoleMessage.USER_GROUP_ID_REQUIRED);
    }
    UserRoleDTO filter = new UserRoleDTO();
    filter.setRoleId(request.getRoleId());
    filter.setAppUserId(request.getAppUserId());
    filter.setUserGroupId(request.getUserGroupId());
    if (getCount(filter) > 0) {
      throw new ApplicationException(UserRoleMessage.USER_ROLE_ALREADY_EXISTS);
    }
  }

  @Override
  protected void makeUpdates(UserRoleDTO source, UserRole target) {
    // UserRole is immutable after creation, no updates allowed
  }
}
