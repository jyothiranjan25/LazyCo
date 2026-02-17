package com.example.lazyco.entities.University;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UniversityService extends CommonAbstractService<UniversityDTO, University> {
  protected UniversityService(UniversityMapper universityMapper) {
    super(universityMapper);
  }

  @Override
  protected void validateBeforeCreate(UniversityDTO request) {
    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(UniversityMessage.UNIVERSITY_CODE_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(UniversityMessage.UNIVERSITY_NAME_REQUIRED);
    }
    validateUniqueName(request, UniversityMessage.DUPLICATE_UNIVERSITY_NAME);
  }

  @Override
  protected void validateBeforeUpdate(UniversityDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, UniversityMessage.DUPLICATE_UNIVERSITY_NAME);
    }
  }
}
