package com.example.lazyco.backend.core.CriteriaBuilder.FieldFiltering;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldPath {
  String aliasPath() default "";

  String fullyQualifiedPath() default "";
}
