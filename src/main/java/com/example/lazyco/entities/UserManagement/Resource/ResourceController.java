package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.entities.UserManagement.UserGroup.UserGroupDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class ResourceController extends AbstractController<UserGroupDTO> {
  public ResourceController(IAbstractService<UserGroupDTO, ?> abstractService) {
    super(abstractService);
  }
}
