package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerTemplateParam;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Filter.FilterService;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ControllerTemplate<D extends AbstractDTO<D>> {

  private final ControllerTemplateParam<D> controllerTemplateParam;

  public ControllerTemplate(ControllerTemplateParam<D> controllerTemplateParam) {
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<?> template(D incomingRequestDTO) {
    if (incomingRequestDTO.getApiAction() != null && !incomingRequestDTO.getApiAction().isEmpty()) {
      return resolveActionByMethod(incomingRequestDTO);
    } else {
      @SuppressWarnings("unchecked")
      D safetyClone = (D) incomingRequestDTO.clone();
      D processed = execute(incomingRequestDTO);

      // common error handling
      if (Boolean.TRUE.equals(processed.getIsAtomicOperation())
          && Boolean.TRUE.equals(processed.getHasError())) {
        return ResponseUtils.sendResponse(HttpStatus.BAD_REQUEST, processed);
      }

      // If GET request and metadata is requested, add it to the response
      if (isGetRequest() && Boolean.TRUE.equals(processed.getGetFilterMetadata())) {
        processed.setFilterFieldMetadata(
            FilterService.getFilterFieldMetadata(processed.getClass()));
      }

      // Send appropriate response based on request type
      return isPostRequest()
          ? ResponseUtils.sendResponse(HttpStatus.CREATED, processed)
          : ResponseUtils.sendResponse(processed);
    }
  }

  private ResponseEntity<?> resolveActionByMethod(D incomingDTO) {
    if (isPostRequest())
      return controllerTemplateParam.resolvePostAction(incomingDTO.getApiAction(), incomingDTO);
    if (isPatchRequest())
      return controllerTemplateParam.resolvePatchAction(incomingDTO.getApiAction(), incomingDTO);
    if (isDeleteRequest())
      return controllerTemplateParam.resolveDeleteAction(incomingDTO.getApiAction(), incomingDTO);
    return controllerTemplateParam.resolveAction(incomingDTO.getApiAction(), incomingDTO);
  }

  abstract D execute(D t);

  protected boolean isGetRequest() {
    return false;
  }

  protected boolean isPostRequest() {
    return false;
  }

  protected boolean isPatchRequest() {
    return false;
  }

  protected boolean isDeleteRequest() {
    return false;
  }
}
