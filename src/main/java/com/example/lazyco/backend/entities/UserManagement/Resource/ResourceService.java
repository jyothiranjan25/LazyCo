package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractDocClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends AbstractService<ResourceDTO, Resource> {
  protected ResourceService(ResourceMapper resourceMapper) {
    super(resourceMapper);
  }
}
