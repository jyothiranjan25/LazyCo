package com.example.lazyco.backend.core.AbstractClasses.Controller;

import com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents.CreateControllerComponent;
import com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents.DeleteControllerComponent;
import com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents.GetControllerComponent;
import com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents.UpdateControllerComponent;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.QueryParams.QueryParams;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class AbstractController<D extends AbstractDTO<D>>
    implements ControllerTemplateParam<D> {

  protected IAbstractService<D, ?> abstractService;

  private final GetControllerComponent<D> readControllerComponent;

  private final CreateControllerComponent<D> createControllerComponent;

  private final UpdateControllerComponent<D> updateControllerComponent;

  private final DeleteControllerComponent<D> deleteControllerComponent;

  public AbstractController(IAbstractService<D, ?> abstractService) {
    this.abstractService = abstractService;
    this.readControllerComponent = new GetControllerComponent<>(abstractService, this);
    this.createControllerComponent = new CreateControllerComponent<>(abstractService, this);
    this.updateControllerComponent = new UpdateControllerComponent<>(abstractService, this);
    this.deleteControllerComponent = new DeleteControllerComponent<>(abstractService, this);
  }

  @GetMapping
  protected ResponseEntity<?> read(@QueryParams D t) {
    if (!restrictCRUDAction().contains(CRUDEnums.GET)) {
      return readControllerComponent.execute(t);
    }
    return ResponseUtils.sendResponse(
        HttpStatus.METHOD_NOT_ALLOWED, "Request method 'GET' not supported");
  }

  @PostMapping
  protected ResponseEntity<?> create(@RequestBody D t) {
    if (!restrictCRUDAction().contains(CRUDEnums.POST)) {
      return createControllerComponent.execute(t);
    }
    return ResponseUtils.sendResponse(
        HttpStatus.METHOD_NOT_ALLOWED, "Request method 'POST' not supported");
  }

  @PatchMapping
  protected ResponseEntity<?> update(@RequestBody D t) {
    if (!restrictCRUDAction().contains(CRUDEnums.PATCH)) {
      return updateControllerComponent.execute(t);
    }
    return ResponseUtils.sendResponse(
        HttpStatus.METHOD_NOT_ALLOWED, "Request method 'PATCH' not supported");
  }

  @DeleteMapping
  protected ResponseEntity<?> delete(@RequestBody D t) {
    if (!restrictCRUDAction().contains(CRUDEnums.DELETE)) {
      return deleteControllerComponent.execute(t);
    }
    return ResponseUtils.sendResponse(
        HttpStatus.METHOD_NOT_ALLOWED, "Request method 'DELETE' not supported");
  }

  public ResponseEntity<?> resolveAction(String action, D t) {
    return ResponseUtils.sendResponse(
        HttpStatus.METHOD_NOT_ALLOWED, "Request method '" + action + "' not supported");
  }

  public ResponseEntity<?> resolvePostAction(String action, D t) {
    return ResponseUtils.sendResponse(
        HttpStatus.METHOD_NOT_ALLOWED, "Request method '" + action + "' not supported");
  }

  public ResponseEntity<?> resolvePatchAction(String action, D t) {
    return ResponseUtils.sendResponse(
        HttpStatus.METHOD_NOT_ALLOWED, "Request method '" + action + "' not supported");
  }

  public ResponseEntity<?> resolveDeleteAction(String action, D t) {
    return ResponseUtils.sendResponse(
        HttpStatus.METHOD_NOT_ALLOWED, "Request method '" + action + "' not supported");
  }

  public List<CRUDEnums> restrictCRUDAction() {
    return List.of();
  }
}
