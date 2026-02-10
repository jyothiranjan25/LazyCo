package com.example.lazyco.entities.AcademicProgram.ProgramTermMaster;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProgramTermMasterService
    extends CommonAbstractService<ProgramTermMasterDTO, ProgramTermMaster> {
  protected ProgramTermMasterService(ProgramTermMasterMapper programTermMasterMapper) {
    super(programTermMasterMapper);
  }

  @Override
  protected void validateBeforeCreate(ProgramTermMasterDTO requestDTO) {
    if (requestDTO.getProgramTermSystemId() == null) {
      throw new ApplicationException(ProgramTermMasterMessage.PROGRAM_TERM_SYSTEM_ID_IS_REQUIRED);
    }

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(ProgramTermMasterMessage.PROGRAM_TERM_MASTER_NAME_IS_REQUIRED);
    }
    validateUniqueName(requestDTO, ProgramTermMasterMessage.DUPLICATE_PROGRAM_TERM_MASTER_NAME);
  }

  @Override
  protected void validateBeforeUpdate(ProgramTermMasterDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, ProgramTermMasterMessage.DUPLICATE_PROGRAM_TERM_MASTER_NAME);
    }
  }
}
