package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AppUserService extends AbstractService<AppUserDTO, AppUser> {

  protected AppUserService(AppUserMapper appUserMapper, AppUserRepository appUserRepository) {
    super(appUserMapper, appUserRepository);
  }

}