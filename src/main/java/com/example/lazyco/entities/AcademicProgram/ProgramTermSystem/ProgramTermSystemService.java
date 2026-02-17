package com.example.lazyco.entities.AcademicProgram.ProgramTermSystem;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProgramTermSystemService
    extends CommonAbstractService<ProgramTermSystemDTO, ProgramTermSystem> {
  protected ProgramTermSystemService(ProgramTermSystemMapper programTermSystemMapper) {
    super(programTermSystemMapper);
  }

  @Override
  protected void validateBeforeCreate(ProgramTermSystemDTO request) {
    if (request.getAcademicProgramId() == null) {
      throw new ApplicationException(
          ProgramTermSystemMessage.PROGRAM_TERM_SYSTEM_PROGRAM_ID_IS_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(ProgramTermSystemMessage.PROGRAM_TERM_SYSTEM_CODE_IS_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(ProgramTermSystemMessage.PROGRAM_TERM_SYSTEM_NAME_IS_REQUIRED);
    }
    validateUniqueName(request, ProgramTermSystemMessage.DUPLICATE_PROGRAM_TERM_SYSTEM_NAME);
  }

  @Override
  protected void validateBeforeUpdate(ProgramTermSystemDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, ProgramTermSystemMessage.DUPLICATE_PROGRAM_TERM_SYSTEM_NAME);
    }
  }
}
