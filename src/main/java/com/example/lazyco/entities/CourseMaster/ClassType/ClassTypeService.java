package com.example.lazyco.entities.CourseMaster.ClassType;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ClassTypeService extends CommonAbstractService<ClassTypeDTO, ClassType> {
  protected ClassTypeService(ClassTypeMapper classTypeMapper) {
    super(classTypeMapper);
  }

  @Override
  protected void validateBeforeCreate(ClassTypeDTO request) {
    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(ClassTypeMessage.CLASS_TYPE_NAME_REQUIRED);
    }
    validateUniqueName(request, ClassTypeMessage.DUPLICATE_CLASS_TYPE);
  }

  @Override
  protected void validateBeforeUpdate(ClassTypeDTO request) {
    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, ClassTypeMessage.DUPLICATE_CLASS_TYPE);
    }
  }
}
