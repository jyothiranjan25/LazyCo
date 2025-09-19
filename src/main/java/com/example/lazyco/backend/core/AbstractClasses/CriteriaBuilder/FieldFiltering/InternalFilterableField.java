package com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InternalFilterableField {}
