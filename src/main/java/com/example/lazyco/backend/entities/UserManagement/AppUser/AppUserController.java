package com.example.lazyco.backend.entities.UserManagement.AppUser;

import static com.example.lazyco.backend.core.Utils.APISchema.APP_USER_API;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.FileParams.FileParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APP_USER_API)
public class AppUserController extends AbstractController<AppUserDTO> {

  public AppUserController(IAbstractService<AppUserDTO, ?> abstractService) {
    super(abstractService);
  }

  @PostMapping
  protected ResponseEntity<?> create(@FileParams AppUserDTO t)
      throws HttpRequestMethodNotSupportedException {
    return super.create(t);
  }
}
