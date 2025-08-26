package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents.DeleteServiceComponent;
import org.springframework.http.ResponseEntity;

public class DeleteControllerComponent<D extends AbstractDTO<D>> {

  private final DeleteServiceComponent<D> deleteServiceComponent;

  public DeleteControllerComponent(DeleteServiceComponent<D> deleteServiceComponent) {
    this.deleteServiceComponent = deleteServiceComponent;
  }

  public ResponseEntity<D> execute(D t) {
    return (new ControllerTemplate<D>() {
          @Override
          D execute(D t) {
            return deleteServiceComponent.delete(t);
          }
        })
        .template(t);
  }
}
