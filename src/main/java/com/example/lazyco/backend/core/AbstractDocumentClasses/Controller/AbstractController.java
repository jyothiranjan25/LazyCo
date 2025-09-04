package com.example.lazyco.backend.core.AbstractDocumentClasses.Controller;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.AbstractDocumentClasses.Controller.ControllerComponents.CreateControllerComponent;
import com.example.lazyco.backend.core.AbstractDocumentClasses.Controller.ControllerComponents.DeleteControllerComponent;
import com.example.lazyco.backend.core.AbstractDocumentClasses.Controller.ControllerComponents.GetControllerComponent;
import com.example.lazyco.backend.core.AbstractDocumentClasses.Controller.ControllerComponents.UpdateControllerComponent;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.QueryParams.QueryParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class AbstractController<D extends AbstractDTO<D>> {

  protected IAbstractService<D, ?> abstractService;

  private final GetControllerComponent<D> readControllerComponent;

  private final CreateControllerComponent<D> createControllerComponent;

  private final UpdateControllerComponent<D> updateControllerComponent;

  private final DeleteControllerComponent<D> deleteControllerComponent;

  public AbstractController(IAbstractService<D, ?> abstractService) {
    this.abstractService = abstractService;
    this.readControllerComponent = new GetControllerComponent<>(abstractService);
    this.createControllerComponent = new CreateControllerComponent<>(abstractService);
    this.updateControllerComponent = new UpdateControllerComponent<>(abstractService);
    this.deleteControllerComponent = new DeleteControllerComponent<>(abstractService);
  }

  @GetMapping
  protected ResponseEntity<?> read(@QueryParams D t) {
    if (!restrictCRUDAction().contains(CRUDEnums.GET)) {
      return readControllerComponent.execute(t);
    }
    return ResponseUtils.sendResponse(HttpStatus.FORBIDDEN, "Action not allowed");
  }

  @PostMapping
  protected ResponseEntity<?> create(@RequestBody D t) {
    if (!restrictCRUDAction().contains(CRUDEnums.POST)) {
      return createControllerComponent.execute(t);
    }
    return ResponseUtils.sendResponse(HttpStatus.FORBIDDEN, "Action not allowed");
  }

  @PatchMapping
  protected ResponseEntity<?> update(@RequestBody D t) {
    if (!restrictCRUDAction().contains(CRUDEnums.PATCH)) {
      return updateControllerComponent.execute(t);
    }
    return ResponseUtils.sendResponse(HttpStatus.FORBIDDEN, "Action not allowed");
  }

  @DeleteMapping
  protected ResponseEntity<?> delete(@RequestBody D t) {
    if (!restrictCRUDAction().contains(CRUDEnums.DELETE)) {
      return deleteControllerComponent.execute(t);
    }
    return ResponseUtils.sendResponse(HttpStatus.FORBIDDEN, "Action not allowed");
  }

  protected List<CRUDEnums> restrictCRUDAction() {
    return List.of();
  }
}
