package com.example.lazyco.backend.core.AbstractClasses.Entity;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserGroup.UserGroupDTO;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AbstractModelListener {

  @PrePersist
  public void prePersist(AbstractModel source) {
    AppUserDTO appUserDTO = getBean(AbstractAction.class).getLoggedInUser();
    UserGroupDTO userGroupDTO = getBean(AbstractAction.class).getLoggedInUserGroup();

    if (source instanceof AbstractRBACModel modelBase) {
      if (modelBase.getUserGroup() == null) {
        modelBase.setUserGroup(userGroupDTO.getFullyQualifiedName());
      }
    }
    source.setCreatedBy(appUserDTO.getUserId());
    source.setCreatedAt(DateTimeZoneUtils.getCurrentDate());
  }

  @PreUpdate
  public void preUpdate(AbstractModel source) {
    AppUserDTO appUserDTO = getBean(AbstractAction.class).getLoggedInUser();
    source.setUpdatedBy(appUserDTO.getUserId());
    source.setCreatedAt(DateTimeZoneUtils.getCurrentDate());
  }
}
