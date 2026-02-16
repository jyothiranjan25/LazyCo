package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.core.Utils.CRUDEnums;
import com.example.lazyco.core.Utils.ResponseUtils;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form")
public class ApplicationFormController extends AbstractController<ApplicationFormDTO> {

  private final ApplicationFormService applicationFormService;

  public ApplicationFormController(IAbstractService<ApplicationFormDTO, ?> abstractService) {
    super(abstractService);
    this.applicationFormService = (ApplicationFormService) abstractService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createForm(@RequestBody Map<String, Object> requestDTO) {
    return ResponseUtils.sendResponse(applicationFormService.createCustomForm(requestDTO));
  }

  @Override
  public List<CRUDEnums> restrictCRUDAction() {
    return List.of(CRUDEnums.POST);
  }
}
