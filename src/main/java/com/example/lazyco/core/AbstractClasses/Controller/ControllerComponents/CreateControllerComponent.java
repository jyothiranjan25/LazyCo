package com.example.lazyco.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.core.AbstractClasses.Controller.ControllerTemplate;
import com.example.lazyco.core.AbstractClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Service.ServiceComponents.CreateServiceComponent;
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

  public ResponseEntity<?> execute(D incomingRequest) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          public D execute(D t) {
            t.setDirectMethodCall(false);
            return createServiceComponent.create(t);
          }

          @Override
          protected boolean isPostRequest() {
            return true;
          }
        })
        .template(incomingRequest);
  }
}
