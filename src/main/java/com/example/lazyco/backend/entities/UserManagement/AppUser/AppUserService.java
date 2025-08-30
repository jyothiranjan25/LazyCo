package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.backend.core.Exceptions.CommonMessage;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AppUserService extends AbstractService<AppUserDTO, AppUser> {
  protected AppUserService(AppUserMapper appUserMapper, AppUserRepository appUserRepository) {
    super(appUserMapper, appUserRepository);
  }
}
