package com.example.lazyco.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.core.AbstractClasses.Controller.ControllerTemplate;
import com.example.lazyco.core.AbstractClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Service.ServiceComponents.SearchServiceComponent;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class SearchControllerComponent<D extends AbstractDTO<D>> {

  private final SearchServiceComponent<D> searchServiceComponent;
  private final ControllerTemplateParam<D> controllerTemplateParam;

  public SearchControllerComponent(
      SearchServiceComponent<D> searchServiceComponent,
      ControllerTemplateParam<D> controllerTemplateParam) {
    this.searchServiceComponent = searchServiceComponent;
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<?> execute(D incomingRequestDTO) {
    return (new ControllerTemplate<D>(controllerTemplateParam) {
          @Override
          public D execute(D t) {
            List<D> result = searchServiceComponent.search(t);
            t.setObjects(result);
            return t;
          }

          @Override
          protected boolean isSearchRequest() {
            return true;
          }
        })
        .template(incomingRequestDTO);
  }
}
