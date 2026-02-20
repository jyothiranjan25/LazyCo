package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.Utils.ResponseUtils;
import com.example.lazyco.core.WebMVC.RequestHandling.QueryParams.QueryParams;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application_form")
public class ApplicationFormController {

  private final ApplicationFormService applicationFormService;

  public ApplicationFormController(ApplicationFormService applicationFormService) {
    this.applicationFormService = applicationFormService;
  }

  @GetMapping
  public ResponseEntity<?> get(@QueryParams ApplicationFormDTO request) {
    List<ApplicationFormDTO> applicationForms = applicationFormService.get(request);
    request.setObjects(applicationForms);
    return ResponseUtils.sendResponse(request);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {
    return ResponseUtils.sendResponse(applicationFormService.createCustomForm(request));
  }

  @PatchMapping
  public ResponseEntity<?> update(@RequestBody Map<String, Object> request) {
    return ResponseUtils.sendResponse(applicationFormService.updateCustomForm(request));
  }

  @DeleteMapping
  public ResponseEntity<?> delete(@RequestBody ApplicationFormDTO request) {
    return ResponseUtils.sendResponse(applicationFormService.deleteCustomForm(request));
  }

  @PostMapping("/enroll")
  public ResponseEntity<?> enroll(@RequestBody ApplicationFormDTO request) {
    return ResponseUtils.sendResponse(applicationFormService.enrollApplication(request));
  }

  @PostMapping("/un_enroll")
  public ResponseEntity<?> unEnroll(@RequestBody ApplicationFormDTO request) {
    return ResponseUtils.sendResponse(applicationFormService.unEnrollApplication(request));
  }
}
