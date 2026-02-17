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
  protected void validateBeforeCreate(AcademicProgramDTO request) {

    if (request.getInstitutionId() == null) {
      throw new ApplicationException(
          AcademicProgramMessage.ACADEMIC_PROGRAM_INSTITUTION_ID_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(AcademicProgramMessage.ACADEMIC_PROGRAM_CODE_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(AcademicProgramMessage.ACADEMIC_PROGRAM_NAME_REQUIRED);
    }
    validateUniqueName(request, AcademicProgramMessage.DUPLICATE_ACADEMIC_PROGRAM_NAME);
  }

  @Override
  protected void validateBeforeUpdate(AcademicProgramDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, AcademicProgramMessage.DUPLICATE_ACADEMIC_PROGRAM_NAME);
    }
  }
}
