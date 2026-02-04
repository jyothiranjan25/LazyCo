package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class ModuleService extends AbstractService<ModuleDTO, Module> {
  protected ModuleService(ModuleMapper moduleMapper) {
    super(moduleMapper);
  }
}
