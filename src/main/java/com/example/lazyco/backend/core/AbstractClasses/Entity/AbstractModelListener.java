package com.example.lazyco.backend.core.AbstractClasses.Entity;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserGroup.UserGroupDTO;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AbstractModelListener {

  @PrePersist
  public void prePersist(AbstractModel source) {
    if (source instanceof AbstractRBACModel modelBase) {
      AbstractAction action = getBean(AbstractAction.class);
      String userGroup;
      if (action.isSystemJob()) {
        userGroup = action.getSystemJobUserGroup();
      } else {
        UserGroupDTO userGroupDTO = action.getLoggedInUserGroup();
        userGroup = userGroupDTO != null ? userGroupDTO.getFullyQualifiedName() : null;
      }
      if (userGroup == null || userGroup.isEmpty()) {
        throw new ExceptionWrapper("Cannot proceed: user group information is missing.");
      }
      modelBase.setUserGroup(userGroup);
    }
    source.setCreatedBy(getUserId());
    source.setCreatedAt(DateTimeZoneUtils.getCurrentDate());
  }

  @PreUpdate
  public void preUpdate(AbstractModel source) {
    source.setUpdatedBy(getUserId());
    source.setUpdatedAt(DateTimeZoneUtils.getCurrentDate());
  }

  private String getUserId() {
    AbstractAction action = getBean(AbstractAction.class);
    String userId;
    if (action.isSystemJob()) {
      userId = action.getSystemJobUserId();
    } else {
      AppUserDTO userDTO = action.getLoggedInUser();
      userId = userDTO != null ? userDTO.getEmail() : null;
    }
    if (userId == null || userId.isEmpty()) {
      userId = action.isBypassRBAC() ? "INTERNAL_SERVICE" : "ANONYMOUS_USER";
    }
    return userId;
  }
}
