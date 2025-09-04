package com.example.lazyco.backend.core.AbstractDocumentClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.ServiceComponents.CreateServiceComponent;
import org.springframework.http.ResponseEntity;

public class CreateControllerComponent<D extends AbstractDTO<D>> {

  private final CreateServiceComponent<D> createServiceComponent;

  public CreateControllerComponent(CreateServiceComponent<D> createServiceComponent) {
    this.createServiceComponent = createServiceComponent;
  }

  public ResponseEntity<D> execute(D incomingRequestDTO) {
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
        .template(incomingRequestDTO);
  }
}
