package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TermCycleService extends CommonAbstractService<TermCycleDTO, TermCycle> {
  protected TermCycleService(TermCycleMapper termCycleMapper) {
    super(termCycleMapper);
  }

  @Override
  protected void validateBeforeCreate(TermCycleDTO requestDTO) {

    if (requestDTO.getAcademicYearId() == null) {
      throw new ApplicationException(TermCycleMessage.TERM_CYCLE_ACADEMIC_YEAR_ID_REQUIRED);
    }

    if (requestDTO.getTermMasterId() == null) {
      throw new ApplicationException(TermCycleMessage.TERM_CYCLE_TERM_MASTER_ID_REQUIRED);
    }

    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(TermCycleMessage.TERM_CYCLE_CODE_REQUIRED);
    }
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(TermCycleMessage.TERM_CYCLE_NAME_REQUIRED);
    }
    validateUniqueName(requestDTO, TermCycleMessage.DUPLICATE_TERM_CYCLE_NAME);
  }

  @Override
  protected void validateBeforeUpdate(TermCycleDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, TermCycleMessage.DUPLICATE_TERM_CYCLE_NAME);
    }
  }
}
