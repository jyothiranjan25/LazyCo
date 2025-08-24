package com.example.lazyco.backend.core.AbstractClasses;

import com.example.lazyco.backend.schema.database.AbstractModelBaseSchema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@MappedSuperclass
public abstract class AbstractModelBase extends AbstractBase {

    @Column(name = AbstractModelBaseSchema.USER_GROUP)
    private String userGroup;
}