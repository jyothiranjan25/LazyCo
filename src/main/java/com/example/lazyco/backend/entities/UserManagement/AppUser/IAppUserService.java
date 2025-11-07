package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;

public interface IAppUserService extends IAbstractService<AppUserDTO, AppUser> {
  AppUserDTO getUserByUserIdOrEmail(String userIdOrEmail);
}
