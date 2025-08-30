package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import org.springframework.http.ResponseEntity;

public abstract class ControllerTemplate<D extends AbstractDTO<D>> {

  public ResponseEntity<D> template(D incomingRequestDTO) {
    incomingRequestDTO = execute(incomingRequestDTO);
    if (isPostRequest()) {
      return ResponseUtils.sendResponse(201, incomingRequestDTO);
    } else {
      return ResponseUtils.sendResponse(incomingRequestDTO);
    }
  }

  abstract D execute(D t);

  protected boolean isPostRequest() {
    return false;
  }
}
