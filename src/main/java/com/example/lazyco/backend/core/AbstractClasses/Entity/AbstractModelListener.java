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
      if (modelBase.getUserGroup() == null) {
        UserGroupDTO userGroupDTO = getBean(AbstractAction.class).getLoggedInUserGroup();

        if (userGroupDTO == null) {
          throw new ExceptionWrapper("Cannot proceed: user group information is missing.");
        }
        modelBase.setUserGroup(userGroupDTO.getFullyQualifiedName());
      }
    }
    AppUserDTO appUserDTO = getBean(AbstractAction.class).getLoggedInUser();
    if (appUserDTO == null) {
      throw new ExceptionWrapper("Cannot proceed: user information is missing.");
    }
    source.setCreatedBy(appUserDTO.getUserId());
    source.setCreatedAt(DateTimeZoneUtils.getCurrentDate());
  }

  @PreUpdate
  public void preUpdate(AbstractModel source) {
    AppUserDTO appUserDTO = getBean(AbstractAction.class).getLoggedInUser();
    if (appUserDTO == null) {
      throw new ExceptionWrapper("Cannot proceed: user information is missing.");
    }
    source.setUpdatedBy(appUserDTO.getUserId());
    source.setUpdatedAt(DateTimeZoneUtils.getCurrentDate());
  }
}
