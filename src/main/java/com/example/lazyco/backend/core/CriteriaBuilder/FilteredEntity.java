package com.example.lazyco.backend.core.CriteriaBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FilteredEntity {
    Class<? extends AbstractModel> type();
}
