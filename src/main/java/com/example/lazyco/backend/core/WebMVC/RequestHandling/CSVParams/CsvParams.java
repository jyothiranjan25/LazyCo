package com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvParams {
  String fileParam() default "file";
}
