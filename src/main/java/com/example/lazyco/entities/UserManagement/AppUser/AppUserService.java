package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
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

  @Override
  protected void validateBeforeCreate(AppUserDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getUserId())) {
      throw new ApplicationException(AppUserMessage.USER_ID_REQUIRED);
    }

    if (StringUtils.isEmpty(requestDTO.getEmail())) {
      throw new ApplicationException(AppUserMessage.EMAIL_REQUIRED);
    }

    AppUserDTO filter = new AppUserDTO();
    filter.setUserId(requestDTO.getUserId());
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          AppUserMessage.DUPLICATE_USER_ID, new Object[] {requestDTO.getUserId()});
    }
    filter = new AppUserDTO();
    filter.setEmail(requestDTO.getEmail().toLowerCase());
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          AppUserMessage.EMAIL_IN_USE, new Object[] {requestDTO.getEmail()});
    }
  }

  protected void preCreate(AppUserDTO dtoToCreate, AppUser entityToCreate) {
    if (entityToCreate.getPassword() != null) {
      String encodedPassword = passwordEncoder.encode(dtoToCreate.getPassword());
      entityToCreate.setPassword(encodedPassword);
    }
    if (entityToCreate.getEmail() != null) {
      entityToCreate.setEmail(entityToCreate.getEmail().toLowerCase());
    }
  }

  @Override
  protected void makeUpdates(AppUserDTO source, AppUser target) {
    super.makeUpdates(source, target);
    if (Boolean.TRUE.equals(source.getClearResetPasswordToken())) {
      target.setResetPasswordToken(null);
      target.setResetPasswordTokenExpiry(null);
    }
    if (source.getEmail() != null) {
      target.setEmail(source.getEmail().toLowerCase());
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
