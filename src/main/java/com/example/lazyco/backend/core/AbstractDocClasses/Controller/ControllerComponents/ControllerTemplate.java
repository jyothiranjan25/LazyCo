package com.example.lazyco.backend.core.AbstractDocClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractDocClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public abstract class ControllerTemplate<D extends AbstractDTO<D>> {

  private final ControllerTemplateParam<D> controllerTemplateParam;

  public ControllerTemplate(ControllerTemplateParam<D> controllerTemplateParam) {
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<D> template(D incomingRequestDTO) {
    if (incomingRequestDTO.getApiAction() != null) {
      return controllerTemplateParam.resolveAction(
          incomingRequestDTO.getApiAction(), incomingRequestDTO);
    } else {
      incomingRequestDTO = execute(incomingRequestDTO);
      if (isPostRequest()) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(incomingRequestDTO);
      } else {
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(incomingRequestDTO);
      }
    }
  }

  abstract D execute(D t);

  protected boolean isPostRequest() {
    return false;
  }
}
