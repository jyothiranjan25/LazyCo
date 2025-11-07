package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppUserService extends AbstractService<AppUserDTO, AppUser>
    implements IAppUserService {
  protected AppUserService(AppUserMapper appUserMapper) {
    super(appUserMapper);
  }

  public AppUserDTO getUserByUserIdOrEmail(String userIdOrEmail) {
    AppUserDTO appUserDTO = new AppUserDTO();
    appUserDTO.setUserId(userIdOrEmail);
    appUserDTO = getSingle(appUserDTO);
    if (appUserDTO == null) {
      appUserDTO = new AppUserDTO();
      appUserDTO.setEmail(userIdOrEmail);
      appUserDTO = getSingle(appUserDTO);
    }
    return appUserDTO;
  }
}
