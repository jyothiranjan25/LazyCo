package com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class ServiceOperationTemplate<D extends AbstractDTO<D>> {

  IAbstractService<D> service;
  BiConsumer<D, AbstractService<D, ?>> postException;

  public ServiceOperationTemplate(
      IAbstractService<D> service, BiConsumer<D, AbstractService<D, ?>> postException) {
    this.postException = postException;
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
      postException.accept(dto, (AbstractService<D, ?>) service);
    }
    return dto;
  }

  public static <D extends AbstractDTO<D>> D executeServiceOperationTemplate(
      ServiceOperationTemplate<D> serviceOperationTemplate, D dto) {
    return serviceOperationTemplate.execute(dto);
  }
}
