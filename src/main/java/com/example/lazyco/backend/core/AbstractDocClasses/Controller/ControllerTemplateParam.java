package com.example.lazyco.backend.core.AbstractDocClasses.Controller;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ControllerTemplateParam<D extends AbstractDTO<D>> {
  ResponseEntity<D> resolveAction(String action, D t);

  List<CRUDEnums> restrictCRUDAction();

  String getResponseListKey();
}
