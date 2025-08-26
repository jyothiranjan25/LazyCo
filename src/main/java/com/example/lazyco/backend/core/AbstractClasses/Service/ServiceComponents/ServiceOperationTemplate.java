package com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceOperationTemplate<D extends AbstractDTO<D>> {

  AbstractService<D,?> service;

  public ServiceOperationTemplate(AbstractService<D,?> service) {
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
          D result = execute(object);
          successList.add(result);
        } catch (Throwable t) {
          hasErrors = true;
          object.setErrorMessage(t.getMessage());
          errorList.add(object);
        }
      }
      resultList.addAll(successList);
      resultList.addAll(errorList);
      dto.setObjectsList(resultList);
    } else {
      // Single object processing
      dto = execute(dto);
    }
    if (hasErrors && (dto.getIsAtomicOperation() != null && dto.getIsAtomicOperation())) {
      dto.setErrorMessage(
          "Atomic operation failed. Rolled back "
              + successList.size()
              + " successful operations. "
              + errorList.size()
              + " errors occurred.");
      // Rollback successful operations
      service.markRollback(dto);
    }
    return dto;
  }

  public static <D extends AbstractDTO<D>> D executeServiceOperationTemplate(
      ServiceOperationTemplate<D> serviceOperationTemplate, D dto) {
    return serviceOperationTemplate.template(dto);
  }
}
