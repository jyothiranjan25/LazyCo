package com.example.lazyco.backend.core.AbstractClasses.Controller;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ControllerTemplateParam<D extends AbstractDTO<D>> {
  ResponseEntity<D> resolveAction(String action, D t);

  ResponseEntity<D> resolvePostAction(String action, D t);

  ResponseEntity<D> resolvePatchAction(String action, D t);

  ResponseEntity<D> resolveDeleteAction(String action, D t);

  List<CRUDEnums> restrictCRUDAction();
}
