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
  protected void validateBeforeCreate(InstitutionDTO requestDTO) {

    if (requestDTO.getUniversityId() == null) {
      throw new ApplicationException(InstitutionMessage.UNIVERSITY_ID_REQUIRED);
    }

    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(InstitutionMessage.INSTITUTION_CODE_REQUIRED);
    }
    //    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(InstitutionMessage.INSTITUTION_NAME_REQUIRED);
    }
    //    validateUniqueName(requestDTO, InstitutionMessage.DUPLICATE_INSTITUTION_NAME);
  }

  @Override
  protected void validateBeforeUpdate(InstitutionDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, InstitutionMessage.DUPLICATE_INSTITUTION_NAME);
    }
  }
}
