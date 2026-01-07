package com.example.lazyco.core.AbstractClasses.CriteriaBuilder;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FilteredEntity {
  Class<? extends AbstractModel> type();
}
