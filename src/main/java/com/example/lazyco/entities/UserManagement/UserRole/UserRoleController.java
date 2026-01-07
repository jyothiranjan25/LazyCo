package com.example.lazyco.entities.UserManagement.UserRole;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user_role")
public class UserRoleController extends AbstractController<UserRoleDTO> {
  public UserRoleController(IAbstractService<UserRoleDTO, ?> abstractService) {
    super(abstractService);
  }
}
