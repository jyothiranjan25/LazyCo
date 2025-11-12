package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUser;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AuthorityEnum;
import com.google.gson.annotations.Expose;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@FilteredEntity(type = AppUser.class)
public class UserDTO extends AbstractDTO<UserDTO> implements UserDetails {
  private String username;

  @Expose(serialize = false)
  private String password;

  private String email;
  private List<AuthorityEnum> authorities;
  private String token;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (authorities == null) {
      return List.of();
    }
    return authorities.stream().map(role -> (GrantedAuthority) role::name).toList();
  }

  public boolean isAccountNonExpired() {
    return true;
  }

  public boolean isAccountNonLocked() {
    return true;
  }

  public boolean isCredentialsNonExpired() {
    return true;
  }

  public boolean isEnabled() {
    return true;
  }
}
