package com.example.lazyco.entities.AcademicProgram;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AcademicProgramService
    extends CommonAbstractService<AcademicProgramDTO, AcademicProgram> {
  protected AcademicProgramService(AcademicProgramMapper academicProgramMapper) {
    super(academicProgramMapper);
  }

  @Override
  protected void validateBeforeCreate(AcademicProgramDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(AcademicProgramMessage.ACADEMIC_PROGRAM_CODE_REQUIRED);
    }
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(AcademicProgramMessage.ACADEMIC_PROGRAM_NAME_REQUIRED);
    }
    validateUniqueName(requestDTO, AcademicProgramMessage.DUPLICATE_ACADEMIC_PROGRAM_NAME);
  }

  @Override
  protected void validateBeforeUpdate(AcademicProgramDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, AcademicProgramMessage.DUPLICATE_ACADEMIC_PROGRAM_NAME);
    }
  }
}
