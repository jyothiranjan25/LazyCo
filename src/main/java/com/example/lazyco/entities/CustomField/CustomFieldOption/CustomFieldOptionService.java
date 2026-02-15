package com.example.lazyco.entities.CustomField.CustomFieldOption;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class CustomFieldOptionService
    extends CommonAbstractService<CustomFieldOptionDTO, CustomFieldOption> {
  protected CustomFieldOptionService(CustomFieldOptionMapper customFieldOptionMapper) {
    super(customFieldOptionMapper);
  }
}
