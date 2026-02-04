package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/module")
public class ModuleController extends AbstractController<ModuleDTO> {
  public ModuleController(IAbstractService<ModuleDTO, ?> abstractService) {
    super(abstractService);
  }
}
