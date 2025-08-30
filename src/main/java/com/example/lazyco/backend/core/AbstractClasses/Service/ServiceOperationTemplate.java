package com.example.lazyco.backend.core.AbstractClasses.Service;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

public abstract class ServiceOperationTemplate<D extends AbstractDTO<D>> {

  AbstractService<D, ?> service;

  public ServiceOperationTemplate(AbstractService<D, ?> service) {
    this.service = service;
  }

  public abstract D execute(D incomingDTO);

  public D template(D incomingDTO) {
    List<D> resultList = new ArrayList<>();
    List<D> successList = new ArrayList<>();
    List<D> errorList = new ArrayList<>();
    if (incomingDTO.getObjectsList() != null && !incomingDTO.getObjectsList().isEmpty()) {
      // Process all objects but track errors for atomic rollback
      for (D object : incomingDTO.getObjectsList()) {
        try {
          D result = executeOptimisticLockWithRetry(object);
          successList.add(result);
        } catch (Throwable t) {
          incomingDTO.setHasError(true);
          object.setErrorMessage("Something went wrong");
          errorList.add(object);
          ApplicationLogger.error(t.getMessage(), t);
        }
      }
      resultList.addAll(successList);
      resultList.addAll(errorList);
      incomingDTO.setObjectsList(resultList);
    } else {
      // Single object processing
      incomingDTO = executeOptimisticLockWithRetry(incomingDTO);
    }

    // Set the error Message to DTO
    if (Boolean.TRUE.equals(incomingDTO.getHasError())) {
      incomingDTO.setErrorMessage(
          "Atomic operation failed. Rolled back "
              + successList.size()
              + " successful operations. "
              + errorList.size()
              + " errors occurred.");
    }

    // Rollback if atomic operation and errors occurred
    if (Boolean.TRUE.equals(incomingDTO.getHasError())
        && Boolean.TRUE.equals(incomingDTO.getIsAtomicOperation())) {
      service.markRollback(incomingDTO);
    }
    return incomingDTO;
  }

  private static final int MAX_RETRIES = 3;

  private D executeOptimisticLockWithRetry(D dto) {
    int attempts = 0;
    while (true) {
      try {
        // Create a clone of the DTO to avoid modifying the original during retries
        @SuppressWarnings("unchecked")
        D cloneDto = (D) dto.clone();
        // Attempt to execute the operation
        return execute(cloneDto);
      } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
        attempts++;
        if (attempts >= MAX_RETRIES) {
          throw e; // fail after max retries
        }
        ApplicationLogger.warn("Optimistic lock conflict, retrying... attempt " + attempts);
      }
    }
  }

  public static <D extends AbstractDTO<D>> D executeServiceOperationTemplate(
      ServiceOperationTemplate<D> serviceOperationTemplate, D incomingDTO) {
    return serviceOperationTemplate.template(incomingDTO);
  }
}
