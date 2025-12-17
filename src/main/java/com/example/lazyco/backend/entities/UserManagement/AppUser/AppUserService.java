package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService extends AbstractService<AppUserDTO, AppUser>
    implements IAppUserService {

  private final PasswordEncoder passwordEncoder;

  protected AppUserService(AppUserMapper appUserMapper, PasswordEncoder passwordEncoder) {
    super(appUserMapper);
    this.passwordEncoder = passwordEncoder;
  }

  protected void preCreate(AppUserDTO dtoToCreate, AppUser entityToCreate) {
    if (entityToCreate.getPassword() != null) {
      String encodedPassword = passwordEncoder.encode(dtoToCreate.getPassword());
      entityToCreate.setPassword(encodedPassword);
    }
  }

  @Override
  protected void makeUpdates(AppUserDTO source, AppUser target) {
    super.makeUpdates(source, target);
    if (source.getResetPasswordToken() == null) {
      target.setResetPasswordToken(null);
    }
    if (source.getResetPasswordTokenExpiry() == null) {
      target.setResetPasswordTokenExpiry(null);
    }
  }

  protected void preUpdate(
      AppUserDTO dtoToUpdate, AppUser entityBeforeUpdates, AppUser entityAfterUpdates) {
    if (dtoToUpdate.getPassword() != null) {
      String encodedPassword = passwordEncoder.encode(dtoToUpdate.getPassword());
      entityAfterUpdates.setPassword(encodedPassword);
    }
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
