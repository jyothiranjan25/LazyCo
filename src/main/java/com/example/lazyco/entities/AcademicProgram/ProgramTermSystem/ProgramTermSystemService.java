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
  protected void validateBeforeCreate(ProgramTermSystemDTO requestDTO) {
    if (requestDTO.getAcademicProgramId() == null) {
      throw new ApplicationException(
          ProgramTermSystemMessage.PROGRAM_TERM_SYSTEM_PROGRAM_ID_IS_REQUIRED);
    }

    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(ProgramTermSystemMessage.PROGRAM_TERM_SYSTEM_CODE_IS_REQUIRED);
    }
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(ProgramTermSystemMessage.PROGRAM_TERM_SYSTEM_NAME_IS_REQUIRED);
    }
    validateUniqueName(requestDTO, ProgramTermSystemMessage.DUPLICATE_PROGRAM_TERM_SYSTEM_NAME);
  }

  @Override
  protected void validateBeforeUpdate(ProgramTermSystemDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, ProgramTermSystemMessage.DUPLICATE_PROGRAM_TERM_SYSTEM_NAME);
    }
  }
}
