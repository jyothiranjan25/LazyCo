package com.example.lazyco.backend.schema.database;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AppUserSchema extends AbstractBaseSchema {

  // Table name
  public static final String TABLE_NAME = "app_user";

  // Column names
  public static final String USER_ID = "user_id";
  public static final String PASSWORD = "password";
  public static final String EMAIL = "email";
  public static final String FIRST_NAME = "first_name";
  public static final String LAST_NAME = "last_name";
}
