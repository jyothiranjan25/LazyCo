package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class AcademicYearService extends AbstractService<AcademicYearDTO, AcademicYear> {
  protected AcademicYearService(AcademicYearMapper academicYearMapper) {
    super(academicYearMapper);
  }
}
