package com.example.lazyco.backend.entities.UserManagement.AppUser;

import static com.example.lazyco.backend.schema.api.APISchema.APP_USER_API;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.QueryParams.QueryParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APP_USER_API)
public class AppUserController extends AbstractController<AppUserDTO> {

    public AppUserController(IAbstractService<AppUserDTO> abstractService) {
        super(abstractService);
    }
}
