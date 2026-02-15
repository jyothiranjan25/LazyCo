package com.example.lazyco.entities.CustomField;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/custom_field")
public class CustomFieldController extends AbstractController<CustomFieldDTO> {
  public CustomFieldController(IAbstractService<CustomFieldDTO, ?> abstractService) {
    super(abstractService);
  }
}
