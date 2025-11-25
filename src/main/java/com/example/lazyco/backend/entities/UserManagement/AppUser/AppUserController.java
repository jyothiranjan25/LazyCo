package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvParams;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app_user")
public class AppUserController extends AbstractController<AppUserDTO> {

  private final ObjectProvider<AppUserUploader> appUserUploader;

  public AppUserController(
      IAppUserService service, ObjectProvider<AppUserUploader> appUserUploader) {
    super(service);
    this.appUserUploader = appUserUploader;
  }

  @PostMapping("csv")
  public ResponseEntity<?> uploadCsv(@CsvParams AppUserDTO appUserDTO) {
    appUserUploader.getObject().executeJob(appUserDTO);
    return ResponseUtils.sendResponse("CSV upload initiated successfully.");
  }
}
