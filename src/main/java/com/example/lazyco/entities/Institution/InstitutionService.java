package com.example.lazyco.entities.Institution;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class InstitutionService extends CommonAbstractService<InstitutionDTO, Institution> {
  protected InstitutionService(InstitutionMapper institutionMapper) {
    super(institutionMapper);
  }

  @Override
  protected void validateBeforeCreate(InstitutionDTO request) {

    if (request.getUniversityId() == null) {
      throw new ApplicationException(InstitutionMessage.UNIVERSITY_ID_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(InstitutionMessage.INSTITUTION_CODE_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(InstitutionMessage.INSTITUTION_NAME_REQUIRED);
    }
    validateUniqueName(request, InstitutionMessage.DUPLICATE_INSTITUTION_NAME);
  }

  @Override
  protected void validateBeforeUpdate(InstitutionDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, InstitutionMessage.DUPLICATE_INSTITUTION_NAME);
    }
  }
}
