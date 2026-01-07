package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user_group")
public class UserGroupController extends AbstractController<UserGroupDTO> {
  public UserGroupController(IAbstractService<UserGroupDTO, ?> abstractService) {
    super(abstractService);
  }
}
