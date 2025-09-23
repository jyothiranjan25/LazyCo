package com.example.lazyco.backend.core.AbstractClasses.Filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FilterableField {

  String displayName() default "";

  String description() default "";

  int displayOrder() default Integer.MIN_VALUE;

  String type() default "";

  boolean sortable() default false;
}
