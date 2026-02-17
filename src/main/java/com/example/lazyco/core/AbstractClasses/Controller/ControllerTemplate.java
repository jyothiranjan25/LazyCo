package com.example.lazyco.core.AbstractClasses.Controller;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Filter.FilterService;
import com.example.lazyco.core.Utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ControllerTemplate<D extends AbstractDTO<D>> {

  private final ControllerTemplateParam<D> controllerTemplateParam;

  public ControllerTemplate(ControllerTemplateParam<D> controllerTemplateParam) {
    this.controllerTemplateParam = controllerTemplateParam;
  }

  public ResponseEntity<?> template(D incomingRequest) {
    if (incomingRequest.getApiAction() != null && !incomingRequest.getApiAction().isEmpty()) {
      return resolveActionByMethod(incomingRequest);
    } else {
      D processed = execute(incomingRequest);

      // common error handling
      if (Boolean.TRUE.equals(processed.getIsAtomicOperation())
          && Boolean.TRUE.equals(processed.getHasError())) {
        return ResponseUtils.sendResponse(HttpStatus.BAD_REQUEST, processed);
      }

      // If there are messages with errors in the incoming DTO, return bad request
      if (incomingRequest.getMessages() != null
          && incomingRequest.getMessages().hasErrors()
          && Boolean.TRUE.equals(incomingRequest.getHasError())) {
        return ResponseUtils.sendResponse(HttpStatus.BAD_REQUEST, incomingRequest);
      }

      // If there are messages with errors in the processed DTO, return bad request
      if (processed.getMessages() != null
          && processed.getMessages().hasErrors()
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

  private ResponseEntity<?> resolveActionByMethod(D incomingRequest) {
    if (isPostRequest())
      return controllerTemplateParam.resolvePostAction(
          incomingRequest.getApiAction(), incomingRequest);
    if (isPatchRequest())
      return controllerTemplateParam.resolvePatchAction(
          incomingRequest.getApiAction(), incomingRequest);
    if (isDeleteRequest())
      return controllerTemplateParam.resolveDeleteAction(
          incomingRequest.getApiAction(), incomingRequest);
    return controllerTemplateParam.resolveAction(incomingRequest.getApiAction(), incomingRequest);
  }

  protected abstract D execute(D t);

  protected boolean isSearchRequest() {
    return false;
  }

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
