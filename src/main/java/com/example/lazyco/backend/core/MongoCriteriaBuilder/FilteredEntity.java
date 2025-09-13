package com.example.lazyco.backend.core.MongoCriteriaBuilder;

import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FilteredEntity {
  Class<? extends AbstractModel> type();
}
