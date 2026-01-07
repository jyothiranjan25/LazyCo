package com.example.lazyco.backend.entities.UserManagement.AppUser;

import java.util.List;

public enum AuthorityEnum {
  ROLE_ADMIN,
  ROLE_FACULTY,
  ROLE_EMPLOYEE,
  ROLE_STUDENT,
  OTHER,
  ;

  AuthorityEnum fromString(String string) {
    return AuthorityEnum.valueOf(string);
  }

  public static List<AuthorityEnum> getAuthority() {
    return List.of(AuthorityEnum.values());
  }
}
