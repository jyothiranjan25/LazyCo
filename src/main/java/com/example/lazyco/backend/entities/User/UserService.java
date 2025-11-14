package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractModelMapper;
import com.example.lazyco.backend.core.Messages.CustomMessage;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserService;
import java.util.function.Consumer;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private AppUserService appUserService;
  private AbstractAction abstractAction;

  @Autowired
  public void injectDependencies(AppUserService appUserService, AbstractAction abstractAction) {
    this.appUserService = appUserService;
    this.abstractAction = abstractAction;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDTO user = getUser(username);
    if (user == null) {
      throw new UsernameNotFoundException(
          CustomMessage.getMessageString(UserMessage.USER_NOT_FOUND, username));
    }
    return user;
  }

  public UserDTO getUser(String userName) {
    abstractAction.setBypassRBAC(true);
    try {
      if (userName.isEmpty()) {
        return null;
      }
      AppUserDTO appUserDTO = appUserService.getUserByUserIdOrEmail(userName);
      if (appUserDTO == null) {
        return null;
      }

      return new AbstractModelMapper()
          .map(
              appUserDTO,
              UserDTO.class,
              (Consumer<TypeMap<AppUserDTO, UserDTO>>)
                  typeMap -> {
                    typeMap.addMappings(
                        mapper -> {
                          mapper.map(AppUserDTO::getUserId, UserDTO::setUsername);
                        });
                  });
    } finally {
      abstractAction.setBypassRBAC(false);
    }
  }

  public AppUserDTO getUserById(Long id) {
    abstractAction.setBypassRBAC(true);
    try {
      if (id == null) {
        return null;
      }
      return appUserService.getById(id);
    } finally {
      abstractAction.setBypassRBAC(false);
    }
  }
}
