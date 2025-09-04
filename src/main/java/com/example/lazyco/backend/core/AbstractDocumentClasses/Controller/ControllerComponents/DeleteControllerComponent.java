package com.example.lazyco.backend.core.AbstractDocumentClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.ServiceComponents.DeleteServiceComponent;
import org.springframework.http.ResponseEntity;

public class DeleteControllerComponent<D extends AbstractDTO<D>> {

  private final DeleteServiceComponent<D> deleteServiceComponent;

  public DeleteControllerComponent(DeleteServiceComponent<D> deleteServiceComponent) {
    this.deleteServiceComponent = deleteServiceComponent;
  }

  public ResponseEntity<D> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>() {
          @Override
          D execute(D requestDTO) {
            return deleteServiceComponent.delete(requestDTO);
          }
        })
        .template(incomingRequestDTO);
  }
}
