package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents.UpdateServiceComponent;
import org.springframework.http.ResponseEntity;

public class UpdateControllerComponent<D extends AbstractDTO<D>> {

  private final UpdateServiceComponent<D> updateServiceComponent;

  public UpdateControllerComponent(UpdateServiceComponent<D> updateServiceComponent) {
    this.updateServiceComponent = updateServiceComponent;
  }

  public ResponseEntity<D> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>() {
          @Override
          D execute(D t) {
            return updateServiceComponent.update(t);
          }
        })
        .template(incomingRequestDTO);
  }
}
