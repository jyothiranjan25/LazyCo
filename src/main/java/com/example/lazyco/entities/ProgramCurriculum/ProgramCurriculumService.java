package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ProgramCurriculumService
    extends CommonAbstractService<ProgramCurriculumDTO, ProgramCurriculum> {
  protected ProgramCurriculumService(ProgramCurriculumMapper programCurriculumMapper) {
    super(programCurriculumMapper);
  }
}
