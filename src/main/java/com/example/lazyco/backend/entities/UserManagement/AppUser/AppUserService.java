package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.backend.core.DateUtils.DateRangeDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AppUserService extends AbstractService<AppUserDTO, AppUser> {
  protected AppUserService(AppUserMapper appUserMapper) {
    super(appUserMapper);
  }

  @Override
  public AppUserDTO create(AppUserDTO dto) {
    return dto;
  }
}
