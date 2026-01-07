package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;

public interface IAppUserService extends IAbstractService<AppUserDTO, AppUser> {
  AppUserDTO getUserByUserIdOrEmail(String userIdOrEmail);
}
