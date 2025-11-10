package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserService implements UserDetailsService {

  private AppUserService appUserService;

  @Autowired
  public void injectDependencies(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDTO user = getUser(username);
    if (user == null) {
      throw new UsernameNotFoundException("could not find user..!!");
    }
    return user;
  }

  public UserDTO getUser(String userName) {
    if (userName.isEmpty()) {
      return null;
    }
    AppUserDTO appUserDTO = appUserService.getUserByUserIdOrEmail(userName);
    if (appUserDTO == null) {
      return null;
    }
    UserDTO userDTO = new UserDTO();
    mapUser(appUserDTO, userDTO);
    return userDTO;
  }

  // Map AppUserDTO to UserDTO
  private void mapUser(AppUserDTO appUserDTO, UserDTO userDTO) {
    userDTO.setId(appUserDTO.getId());
    userDTO.setUsername(appUserDTO.getUserId());
    userDTO.setPassword(appUserDTO.getPassword());
    userDTO.setEmail(appUserDTO.getEmail());
    userDTO.setAuthorities(new ArrayList<>(appUserDTO.getAuthorities()));
  }
}
