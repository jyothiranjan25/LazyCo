package com.example.lazyco.backend.entities.UserManagement.AppUser;

import static com.example.lazyco.backend.core.Utils.APISchema.APP_USER_API;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APP_USER_API)
public class AppUserController extends AbstractController<AppUserDTO> {

  private final AppUserUploader appUserUploader;

  public AppUserController(
      IAbstractService<AppUserDTO, ?> abstractService, AppUserUploader appUserUploader) {
    super(abstractService);
    this.appUserUploader = appUserUploader;
  }

  @PostMapping("csv")
  public ResponseEntity<?> uploadCsv(@CsvParams AppUserDTO inputData) {
    appUserUploader.executeJob(inputData.getObjects());
    return ResponseEntity.ok().build();
  }
}
