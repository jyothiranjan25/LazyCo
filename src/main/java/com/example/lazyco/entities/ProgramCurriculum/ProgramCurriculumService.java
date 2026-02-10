package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProgramCurriculumService
    extends CommonAbstractService<ProgramCurriculumDTO, ProgramCurriculum> {
  protected ProgramCurriculumService(ProgramCurriculumMapper programCurriculumMapper) {
    super(programCurriculumMapper);
  }

  @Override
  protected void validateBeforeCreate(ProgramCurriculumDTO requestDTO) {

    if (requestDTO.getAcademicYearId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_ACADEMIC_YEAR_REQUIRED);
    }

    if (requestDTO.getTermSystemId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_TERM_SYSTEM_REQUIRED);
    }

    if (requestDTO.getAcademicProgramId() == null) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_PROGRAM_REQUIRED);
    }

    if (requestDTO.getProgramTermSystemId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_TERM_CYCLE_REQUIRED);
    }

    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_CODE_REQUIRED);
    }
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_NAME_REQUIRED);
    }
    validateUniqueName(requestDTO, ProgramCurriculumMessage.DUPLICATE_PROGRAM_CURRICULUM_NAME);
  }

  @Override
  protected void validateBeforeUpdate(ProgramCurriculumDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, ProgramCurriculumMessage.DUPLICATE_PROGRAM_CURRICULUM_NAME);
    }
  }
}
