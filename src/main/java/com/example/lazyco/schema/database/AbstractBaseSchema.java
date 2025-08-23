package com.example.lazyco.schema.database;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractBaseSchema {
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String CREATED_BY = "created_by";
    public static final String UPDATED_BY = "updated_by";
    public static final String HIBERNATE_SEQUENCES = "hibernate_sequences";
}