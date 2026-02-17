package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.AbstractAction;
import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService extends AbstractService<AppUserDTO, AppUser>
    implements IAppUserService {

  private final AbstractAction abstractAction;
  private final PasswordEncoder passwordEncoder;

  protected AppUserService(
      AppUserMapper appUserMapper, AbstractAction abstractAction, PasswordEncoder passwordEncoder) {
    super(appUserMapper);
    this.abstractAction = abstractAction;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected AppUserDTO updateFilterBeforeGet(AppUserDTO filter) {
    // Exclude logged-in user from list to avoid self deletion or updates
    AppUserDTO loggedInUser = abstractAction.getLoggedInUser();
    if (loggedInUser != null && loggedInUser.getId() != null && !loggedInUser.getIsSuperAdmin()) {
      filter.setIdsNotIn(List.of(loggedInUser.getId()));
    }
    return filter;
  }

  @Override
  protected void validateBeforeCreate(AppUserDTO request) {
    if (StringUtils.isEmpty(request.getUserId())) {
      throw new ApplicationException(AppUserMessage.USER_ID_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getEmail())) {
      throw new ApplicationException(AppUserMessage.EMAIL_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getFirstName())) {
      throw new ApplicationException(AppUserMessage.FIRST_NAME_REQUIRED);
    }

    duplicateCheck(request);
  }

  @Override
  protected void preCreate(AppUserDTO dtoToCreate, AppUser entityToCreate) {
    if (entityToCreate.getPassword() != null) {
      String encodedPassword = passwordEncoder.encode(dtoToCreate.getPassword());
      entityToCreate.setPassword(encodedPassword);
    }
    mapAuthorities(entityToCreate);
  }

  @Override
  protected void validateBeforeUpdate(AppUserDTO request) {
    duplicateCheck(request);
  }

  @Override
  protected void makeUpdates(AppUserDTO source, AppUser target) {
    super.makeUpdates(source, target);
    if (Boolean.TRUE.equals(source.getClearResetPasswordToken())) {
      target.setResetPasswordToken(null);
      target.setResetPasswordTokenExpiry(null);
    }
  }

  @Override
  protected void preUpdate(
      AppUserDTO dtoToUpdate, AppUserDTO entityBeforeUpdates, AppUser entityToUpdate) {
    if (dtoToUpdate.getPassword() != null) {
      String encodedPassword = passwordEncoder.encode(dtoToUpdate.getPassword());
      entityToUpdate.setPassword(encodedPassword);
    }
    mapAuthorities(entityToUpdate);
  }

  private void duplicateCheck(AppUserDTO request) {
    abstractAction.pushBypassRBAC(true);
    try {
      AppUserDTO filter = new AppUserDTO();
      if (request.getId() != null) filter.setIdsNotIn(List.of(request.getId()));
      // Check for duplicate userId
      if (!StringUtils.isEmpty(request.getUserId())) {
        filter.setUserId(request.getUserId());
        if (getCount(filter) > 0) {
          throw new ApplicationException(
              AppUserMessage.DUPLICATE_USER_ID, new Object[] {request.getUserId()});
        }
      }

      // Check for duplicate email
      if (!StringUtils.isEmpty(request.getEmail())) {
        filter = new AppUserDTO();
        filter.setEmail(request.getEmail().toLowerCase());
        if (getCount(filter) > 0) {
          throw new ApplicationException(
              AppUserMessage.EMAIL_IN_USE, new Object[] {request.getEmail()});
        }
      }
    } finally {
      abstractAction.popBypassRBAC();
    }
  }

  private void mapAuthorities(AppUser appUser) {
    Set<AuthorityEnum> authorities = new HashSet<>();
    if (appUser.getIsAdministrator() != null && appUser.getIsAdministrator()) {
      authorities.add(AuthorityEnum.ROLE_ADMIN);
    }
    appUser.setAuthorities(authorities);
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
