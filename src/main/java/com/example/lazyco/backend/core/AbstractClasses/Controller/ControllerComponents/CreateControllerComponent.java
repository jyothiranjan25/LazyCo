package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.ServiceComponents.CreateServiceComponent;
import org.springframework.http.ResponseEntity;

public class CreateControllerComponent<D extends AbstractDTO<D>> {

  private final CreateServiceComponent<D> createServiceComponent;
  private final ControllerTemplateParam<D> controllerTemplateParam;

  public CreateControllerComponent(
      CreateServiceComponent<D> createServiceComponent,
      ControllerTemplateParam<D> controllerTemplateParam) {
    this.createServiceComponent = createServiceComponent;
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<D> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          D execute(D t) {
            return createServiceComponent.create(t);
          }

          @Override
          protected boolean isPostRequest() {
            return true;
          }
        })
        .template(incomingRequestDTO);
  }
}
