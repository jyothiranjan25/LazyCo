package com.example.lazyco.entities.TermSystem.TermMaster;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
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
      validateUniqueName(requestDTO, TermMasterMessage.DUPLICATE_TERM_MASTER_NAME);
    }
  }

  @Override
  protected void makeUpdates(
      TermMasterDTO source, TermMaster beforeUpdates, TermMaster afterUpdates) {
    // don't update term system if term system id is not provided in the request
    if (beforeUpdates.getTermSystem() != null) {
      afterUpdates.setTermSystem(beforeUpdates.getTermSystem());
    }
  }
}
