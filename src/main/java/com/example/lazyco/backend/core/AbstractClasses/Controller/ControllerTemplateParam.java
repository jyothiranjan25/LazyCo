package com.example.lazyco.backend.core.AbstractClasses.Controller;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ControllerTemplateParam<D extends AbstractDTO<D>> {
  ResponseEntity<?> resolveAction(String action, D t);

  ResponseEntity<?> resolvePostAction(String action, D t);

  ResponseEntity<?> resolvePatchAction(String action, D t);

  ResponseEntity<?> resolveDeleteAction(String action, D t);

  List<CRUDEnums> restrictCRUDAction();
}
