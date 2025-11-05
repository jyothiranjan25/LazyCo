package com.example.lazyco.backend.entities.UserManagement.AppUser;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app_user")
public class AppUserController extends AbstractController<AppUserDTO> {

  public AppUserController(IAbstractService<AppUserDTO, ?> abstractService) {
    super(abstractService);
  }

  @PostMapping("csv")
  public ResponseEntity<?> uploadCsv(@CsvParams AppUserDTO inputData) {
    getBean(AppUserUploader.class).executeJob(inputData);
    return ResponseUtils.sendResponse("CSV upload initiated successfully.");
  }
}
