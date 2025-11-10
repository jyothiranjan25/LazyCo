package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AuthorityEnum;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
public class UserDTO extends AbstractDTO<UserDTO> implements UserDetails {
  private String username;
  private String password;
  private String email;
  private List<AuthorityEnum> authorities;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities.stream().map(role -> (GrantedAuthority) role::name).toList();
  }

  public boolean isAccountNonExpired() {
    return false;
  }

  public boolean isAccountNonLocked() {
    return false;
  }

  public boolean isCredentialsNonExpired() {
    return true;
  }

  public boolean isEnabled() {
    return true;
  }
}
