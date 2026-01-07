package com.example.lazyco.core.WebMVC.RequestHandling.CSVParams;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvParams {

  boolean dtoAsFileParam() default false;

  String fileParam() default "file";
}
