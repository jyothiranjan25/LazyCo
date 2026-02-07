package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/academic_year")
public class AcademicYearController extends AbstractController<AcademicYearDTO> {
  public AcademicYearController(IAbstractService<AcademicYearDTO, ?> abstractService) {
    super(abstractService);
  }
}
