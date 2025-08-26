package com.example.lazyco.backend.entities.UserManagement.AppUser;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserService {

  private final AppUserRepository appUserRepository;
  private final AppUserMapper appUserMapper;

  public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
    this.appUserRepository = appUserRepository;
    this.appUserMapper = appUserMapper;
  }

  @Transactional(readOnly = true)
  public AppUserDTO get(AppUserDTO appUserDTO) {
    AppUser appUser = appUserMapper.map(appUserDTO);
    List<AppUser> appUsers = appUserRepository.findAll();
    List<AppUserDTO> appUserDTOs = appUserMapper.map(appUsers);
    appUserDTO.setObjectsList(appUserDTOs);
    return appUserDTO;
  }

  @Transactional
  public AppUserDTO create(AppUserDTO appUserDTO) {
    AppUser appUser = appUserMapper.map(appUserDTO);
    appUser = appUserRepository.save(appUser);
    return appUserMapper.map(appUser);
  }

  @Transactional
  public AppUserDTO update(AppUserDTO appUserDTO) {
    AppUser appUser = appUserRepository.findById(appUserDTO.getId()).orElseThrow();
    appUser = appUserMapper.mapDTOToEntity(appUserDTO, appUser);
    appUser = appUserRepository.save(appUser);
    return appUserMapper.map(appUser);
  }

  @Transactional
  public AppUserDTO delete(AppUserDTO appUserDTO) {
    AppUser appUser = appUserRepository.findById(appUserDTO.getId()).orElseThrow();
    AppUserDTO appUserDTO1 = appUserMapper.map(appUser);
    appUserRepository.deleteById(appUserDTO1.getId());
    return appUserDTO1;
  }
}
