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
  protected void validateBeforeCreate(TermCycleDTO request) {

    if (request.getAcademicYearId() == null) {
      throw new ApplicationException(TermCycleMessage.TERM_CYCLE_ACADEMIC_YEAR_ID_REQUIRED);
    }

    if (request.getTermMasterId() == null) {
      throw new ApplicationException(TermCycleMessage.TERM_CYCLE_TERM_MASTER_ID_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(TermCycleMessage.TERM_CYCLE_CODE_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(TermCycleMessage.TERM_CYCLE_NAME_REQUIRED);
    }
    validateUniqueName(request, TermCycleMessage.DUPLICATE_TERM_CYCLE_NAME);
  }

  @Override
  protected void validateBeforeUpdate(TermCycleDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, TermCycleMessage.DUPLICATE_TERM_CYCLE_NAME);
    }
  }
}
