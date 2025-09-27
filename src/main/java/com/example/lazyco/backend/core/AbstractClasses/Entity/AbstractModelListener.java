package com.example.lazyco.backend.core.AbstractClasses.Entity;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.UserGroupDTO;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Date;

public class AbstractModelListener {

  @PrePersist
  public void prePersist(AbstractModel source) {
    AppUserDTO appUserDTO = getBean(AbstractAction.class).getLoggedInUser();
    UserGroupDTO userGroupDTO = getBean(AbstractAction.class).loggedInUserGroup();

    if (source instanceof AbstractRBACModel modelBase) {
      if (modelBase.getUserGroup() == null) {
        modelBase.setUserGroup(userGroupDTO.getFullyQualifiedName());
      }
    }
    source.setCreatedBy(appUserDTO.getUserId());
    source.setCreatedAt(new Date());
  }

  @PreUpdate
  public void preUpdate(AbstractModel source) {
    AppUserDTO appUserDTO = getBean(AbstractAction.class).getLoggedInUser();
    source.setUpdatedBy(appUserDTO.getUserId());
    source.setUpdatedAt(new Date());
  }
}
