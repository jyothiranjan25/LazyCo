package com.example.lazyco.entities.TermSystem.TermMaster;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.entities.TermSystem.TermSystemMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TermMasterService extends CommonAbstractService<TermMasterDTO, TermMaster> {
  protected TermMasterService(TermMasterMapper termMasterMapper) {
    super(termMasterMapper);
  }

  @Override
  protected void validateBeforeCreate(TermMasterDTO requestDTO) {

    if (requestDTO.getTermSystemId() == null) {
      throw new ApplicationException(TermMasterMessage.TERM_MASTER_TERM_SYSTEM_ID_REQUIRED);
    }

    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(TermMasterMessage.TERM_MASTER_CODE_REQUIRED);
    }
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(TermMasterMessage.TERM_MASTER_NAME_REQUIRED);
    }
    validateUniqueName(requestDTO, TermMasterMessage.DUPLICATE_TERM_MASTER_NAME);
  }

  @Override
  protected void validateBeforeUpdate(TermMasterDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, TermSystemMessage.DUPLICATE_TERM_SYSTEM_NAME);
    }
  }
}
