package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enum_display_value")
public class EnumDisplayValueController extends AbstractController<EnumDisplayValueDTO> {

  public EnumDisplayValueController(IEnumDisplayValueService enumDisplayValueService) {
    super(enumDisplayValueService);
  }
}
