package com.example.lazyco.backend.core.AbstractClasses.Service;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.util.ArrayList;
import java.util.List;

public abstract class ServiceOperationTemplate<D extends AbstractDTO<D>> {

  AbstractService<D, ?> service;

  public ServiceOperationTemplate(AbstractService<D, ?> service) {
    this.service = service;
  }

  public abstract D execute(D dto);

  public D template(D dto) {
    boolean hasErrors = false;
    List<D> resultList = new ArrayList<>();
    List<D> successList = new ArrayList<>();
    List<D> errorList = new ArrayList<>();
    if (dto.getObjectsList() != null && !dto.getObjectsList().isEmpty()) {
      // Process all objects but track errors for atomic rollback
      for (D object : dto.getObjectsList()) {
        try {
          D result = template(object);
          successList.add(result);
        } catch (Throwable t) {
          hasErrors = true;
          object.setErrorMessage("Something went wrong");
          errorList.add(object);
          ApplicationLogger.error(t.getMessage(), t);
        }
      }
      resultList.addAll(successList);
      resultList.addAll(errorList);
      dto.setObjectsList(resultList);
    } else {
      // Single object processing
      dto = execute(dto);
    }
    if (hasErrors) {
      dto.setErrorMessage(
          "Atomic operation failed. Rolled back "
              + successList.size()
              + " successful operations. "
              + errorList.size()
              + " errors occurred.");
    }

    // Rollback if atomic operation and errors occurred
    if (hasErrors && dto.getIsAtomicOperation() != null && dto.getIsAtomicOperation()) {
      service.markRollback(dto);
    }

    return dto;
  }

  public static <D extends AbstractDTO<D>> D executeServiceOperationTemplate(
      ServiceOperationTemplate<D> serviceOperationTemplate, D dto) {
    return serviceOperationTemplate.template(dto);
  }
}
