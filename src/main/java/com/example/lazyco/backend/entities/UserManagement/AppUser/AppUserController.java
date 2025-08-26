package com.example.lazyco.backend.entities.UserManagement.AppUser;

import static com.example.lazyco.backend.schema.api.APISchema.APP_USER_API;

import com.example.lazyco.backend.core.WebMVC.RequestHandling.QueryParams.QueryParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APP_USER_API)
public class AppUserController {

  private final AppUserService appUserService;

  public AppUserController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<AppUserDTO> get(@QueryParams AppUserDTO appUserDTO) {
    appUserDTO = appUserService.get(appUserDTO);
    return ResponseEntity.ok(appUserDTO);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<AppUserDTO> create(@RequestBody AppUserDTO appUserDTO) {
    appUserDTO = appUserService.create(appUserDTO);
    return ResponseEntity.ok(appUserDTO);
  }

  @RequestMapping(method = RequestMethod.PATCH)
  public ResponseEntity<AppUserDTO> update(@RequestBody AppUserDTO appUserDTO) {
    appUserDTO = appUserService.update(appUserDTO);
    return ResponseEntity.ok(appUserDTO);
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity<AppUserDTO> delete(@RequestBody AppUserDTO appUserDTO) {
    appUserDTO = appUserService.delete(appUserDTO);
    return ResponseEntity.ok(appUserDTO);
  }
}
