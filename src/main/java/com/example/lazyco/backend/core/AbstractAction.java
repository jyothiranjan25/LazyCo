package com.example.lazyco.backend.core;

import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.UserGroupDTO;
import org.springframework.stereotype.Service;

@Service
public class AbstractAction {

  public static AppUserDTO getLoggedInUser() {
    AppUserDTO appUserDTO = new AppUserDTO();
    appUserDTO.setUserId("JO");
    return appUserDTO;
  }

  public static UserGroupDTO loggedInUserGroup() {
    UserGroupDTO userGroupDTO = new UserGroupDTO();
    userGroupDTO.setFullyQualifiedName("DEFAULT");
    return userGroupDTO;
  }
}
