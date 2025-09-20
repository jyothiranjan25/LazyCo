package com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvParams {
  Class<AbstractDTO> clazz() default AbstractDTO.class;

  String fileParam() default "file";
}
