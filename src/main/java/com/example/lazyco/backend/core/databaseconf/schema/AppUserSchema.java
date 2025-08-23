package com.example.lazyco.backend.core.databaseconf.schema;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AppUserSchema extends AbstractBaseSchema {
    public static final String TABLE_NAME = "app_user";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
}