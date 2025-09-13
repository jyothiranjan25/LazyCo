package com.example.lazyco.backend.core.AbstractDocClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractDocClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents.UpdateServiceComponent;
import org.springframework.http.ResponseEntity;

public class UpdateControllerComponent<D extends AbstractDTO<D>> {

  private final UpdateServiceComponent<D> updateServiceComponent;
  private final ControllerTemplateParam<D> controllerTemplateParam;

  public UpdateControllerComponent(
      UpdateServiceComponent<D> updateServiceComponent,
      ControllerTemplateParam<D> controllerTemplateParam) {
    this.updateServiceComponent = updateServiceComponent;
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<D> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          D execute(D t) {
              try {
                  return updateServiceComponent.update(t);
              }catch (Exception e) {
                  e.printStackTrace();
              }
              return t;
          }
        })
        .template(incomingRequestDTO);
  }
}
