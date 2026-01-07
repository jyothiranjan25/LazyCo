package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController extends AbstractController<RoleDTO> {
  public RoleController(IAbstractService<RoleDTO, ?> abstractService) {
    super(abstractService);
  }
}
