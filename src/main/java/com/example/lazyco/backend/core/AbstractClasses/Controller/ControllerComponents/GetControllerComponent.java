package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents.GetServiceComponent;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class GetControllerComponent<D extends AbstractDTO<D>> {

  private final GetServiceComponent<D> getServiceComponent;

  public GetControllerComponent(GetServiceComponent<D> getServiceComponent) {
    this.getServiceComponent = getServiceComponent;
  }

  public ResponseEntity<D> execute(D t) {
    return (new ControllerTemplate<D>() {
          @Override
          D execute(D t) {
            List<D> list = getServiceComponent.get(t);
            if (!list.isEmpty()) {
              t.setObjectsList(list);
            }
            return t;
          }
        })
        .template(t);
  }
}
