package com.example.lazyco.backend.entities.UserManagement.AppUser;

import java.util.List;

public enum AuthorityEntityEnum {
  ROLE_ADMIN,
  ROLE_FACULTY,
  ROLE_EMPLOYEE,
  ROLE_STUDENT,
  OTHER,
  ;

  AuthorityEntityEnum fromString(String string) {
    return AuthorityEntityEnum.valueOf(string);
  }

  public static List<AuthorityEntityEnum> getAuthority() {
    return List.of(AuthorityEntityEnum.values());
  }
}
