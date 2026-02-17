package com.example.lazyco.entities.AcademicProgram.ProgramSpecialization;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProgramSpecializationService
    extends CommonAbstractService<ProgramSpecializationDTO, ProgramSpecialization> {
  protected ProgramSpecializationService(ProgramSpecializationMapper programSpecializationMapper) {
    super(programSpecializationMapper);
  }

  @Override
  protected void validateBeforeCreate(ProgramSpecializationDTO request) {
    if (request.getAcademicProgramId() == null) {
      throw new ApplicationException(ProgramSpecializationMessage.ACADEMIC_PROGRAM_ID_IS_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(
          ProgramSpecializationMessage.PROGRAM_SPECIALIZATION_CODE_IS_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(
          ProgramSpecializationMessage.PROGRAM_SPECIALIZATION_NAME_IS_REQUIRED);
    }
    validateUniqueName(request, ProgramSpecializationMessage.DUPLICATE_PROGRAM_SPECIALIZATION_NAME);
  }

  @Override
  protected void validateBeforeUpdate(ProgramSpecializationDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(
          request, ProgramSpecializationMessage.DUPLICATE_PROGRAM_SPECIALIZATION_NAME);
    }
  }
}
