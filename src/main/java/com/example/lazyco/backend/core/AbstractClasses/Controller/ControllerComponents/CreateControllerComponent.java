package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents.CreateServiceComponent;
import org.springframework.http.ResponseEntity;

public class CreateControllerComponent<D extends AbstractDTO<D>> {

  private final CreateServiceComponent<D> createServiceComponent;

  public CreateControllerComponent(CreateServiceComponent<D> createServiceComponent) {
    this.createServiceComponent = createServiceComponent;
  }

  public ResponseEntity<D> execute(D t) {
    return (new ControllerTemplate<D>() {
          @Override
          D execute(D t) {
            return createServiceComponent.create(t);
          }

          @Override
          protected boolean isPostRequest() {
            return true;
          }
        })
        .template(t);
  }
}
