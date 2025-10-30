package com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvField {
  String name() default "";

  boolean optional() default false;

  String type() default "String";

  String[] options() default {};

  int order() default Integer.MAX_VALUE;
}
