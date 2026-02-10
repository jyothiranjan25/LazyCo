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
  protected void validateBeforeCreate(ProgramSpecializationDTO requestDTO) {
    if (requestDTO.getAcademicProgramId() == null) {
      throw new ApplicationException(ProgramSpecializationMessage.ACADEMIC_PROGRAM_ID_IS_REQUIRED);
    }

    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(
          ProgramSpecializationMessage.PROGRAM_SPECIALIZATION_CODE_IS_REQUIRED);
    }
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(
          ProgramSpecializationMessage.PROGRAM_SPECIALIZATION_NAME_IS_REQUIRED);
    }
    validateUniqueName(
        requestDTO, ProgramSpecializationMessage.DUPLICATE_PROGRAM_SPECIALIZATION_NAME);
  }

  @Override
  protected void validateBeforeUpdate(ProgramSpecializationDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(
          requestDTO, ProgramSpecializationMessage.DUPLICATE_PROGRAM_SPECIALIZATION_NAME);
    }
  }
}
