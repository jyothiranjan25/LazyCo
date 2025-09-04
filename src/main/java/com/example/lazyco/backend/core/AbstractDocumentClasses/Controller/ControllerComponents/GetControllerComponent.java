package com.example.lazyco.backend.core.AbstractDocumentClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.ServiceComponents.GetServiceComponent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GetControllerComponent<D extends AbstractDTO<D>> {

  private final GetServiceComponent<D> getServiceComponent;

  public GetControllerComponent(GetServiceComponent<D> getServiceComponent) {
    this.getServiceComponent = getServiceComponent;
  }

  public ResponseEntity<D> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>() {
          @Override
          D execute(D t) {
            List<D> result = getServiceComponent.get(t);
            t.setObjectsList(result);
            return t;
          }
        })
        .template(incomingRequestDTO);
  }
}
