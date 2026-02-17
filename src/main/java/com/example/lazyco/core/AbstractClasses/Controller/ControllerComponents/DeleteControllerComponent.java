package com.example.lazyco.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.core.AbstractClasses.Controller.ControllerTemplate;
import com.example.lazyco.core.AbstractClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Service.ServiceComponents.DeleteServiceComponent;
import org.springframework.http.ResponseEntity;

public class DeleteControllerComponent<D extends AbstractDTO<D>> {

  private final DeleteServiceComponent<D> deleteServiceComponent;
  private final ControllerTemplateParam<D> controllerTemplateParam;

  public DeleteControllerComponent(
      DeleteServiceComponent<D> deleteServiceComponent,
      ControllerTemplateParam<D> controllerTemplateParam) {
    this.deleteServiceComponent = deleteServiceComponent;
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<?> execute(D incomingRequest) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          public D execute(D request) {
            return deleteServiceComponent.delete(request);
          }

          @Override
          protected boolean isDeleteRequest() {
            return true;
          }
        })
        .template(incomingRequest);
  }
}
