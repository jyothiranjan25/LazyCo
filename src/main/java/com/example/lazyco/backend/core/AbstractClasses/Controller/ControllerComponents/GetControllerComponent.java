package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.ServiceComponents.GetServiceComponent;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class GetControllerComponent<D extends AbstractDTO<D>> {

  private final GetServiceComponent<D> getServiceComponent;
  private final ControllerTemplateParam<D> controllerTemplateParam;

  public GetControllerComponent(
      GetServiceComponent<D> getServiceComponent,
      ControllerTemplateParam<D> controllerTemplateParam) {
    this.getServiceComponent = getServiceComponent;
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<D> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          D execute(D t) {
            List<D> result = getServiceComponent.get(t);
            t.setObjects(result);
            return t;
          }
        })
        .template(incomingRequestDTO);
  }
}
