package com.example.lazyco.backend.core.abstracts;

import com.example.lazyco.backend.core.databaseconf.schema.AbstractBaseSchema;
import com.example.lazyco.backend.core.databaseconf.schema.AbstractModelBaseSchema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@MappedSuperclass
public abstract class AbstractModelBase extends AbstractBase {

    @Column(name = AbstractModelBaseSchema.USER_GROUP)
    private String userGroup;
}