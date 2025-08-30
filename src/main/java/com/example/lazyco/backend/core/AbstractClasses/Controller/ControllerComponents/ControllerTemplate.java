package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ControllerTemplate<D extends AbstractDTO<D>> {

  public ResponseEntity<D> template(D incomingRequestDTO) {
    incomingRequestDTO = execute(incomingRequestDTO);

    // common error handling
    if (Boolean.TRUE.equals(incomingRequestDTO.getIsAtomicOperation())
        && Boolean.TRUE.equals(incomingRequestDTO.getHasError())) {
      return ResponseUtils.sendResponse(HttpStatus.BAD_REQUEST, incomingRequestDTO);
    }
    if (isPostRequest()) {
      return ResponseUtils.sendResponse(HttpStatus.CREATED, incomingRequestDTO);
    } else {
      return ResponseUtils.sendResponse(incomingRequestDTO);
    }
  }

  abstract D execute(D t);

  protected boolean isPostRequest() {
    return false;
  }
}
