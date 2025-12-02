package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents.GetServiceComponent;
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

  public ResponseEntity<?> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          D execute(D t) {
            List<D> result = getServiceComponent.get(t);
            t.setObjects(result);
            return t;
          }

          @Override
          protected boolean isGetRequest() {
            return true;
          }
        })
        .template(incomingRequestDTO);
  }
}
