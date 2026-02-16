package com.example.lazyco.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.core.AbstractClasses.Controller.ControllerTemplate;
import com.example.lazyco.core.AbstractClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Service.ServiceComponents.UpdateServiceComponent;
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

  public ResponseEntity<?> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          public D execute(D t) {
            return updateServiceComponent.update(t);
          }

          @Override
          protected boolean isPatchRequest() {
            return true;
          }
        })
        .template(incomingRequestDTO);
  }
}
