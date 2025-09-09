package com.example.lazyco.backend.core.AbstractDocClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractDocClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents.DeleteServiceComponent;
import org.springframework.http.ResponseEntity;

public class DeleteControllerComponent<D extends AbstractDTO<D>> {

  private final DeleteServiceComponent<D> deleteServiceComponent;
  private final ControllerTemplateParam<D> controllerTemplateParam;

  public DeleteControllerComponent(
      DeleteServiceComponent<D> deleteServiceComponent,
      ControllerTemplateParam<D> controllerTemplateParam) {
    this.deleteServiceComponent = deleteServiceComponent;
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<D> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          D execute(D requestDTO) {
            return deleteServiceComponent.delete(requestDTO);
          }
        })
        .template(incomingRequestDTO);
  }
}
