package com.example.lazyco.backend.core.AbstractClasses.Service;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.CommonMessage;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Messages.CustomMessage;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatusCode;
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
        } catch (ExceptionWrapper e) {
          incomingDTO.setHasError(true);
          object.setErrorMessage(e.getMessage());
          errorList.add(object);
          ApplicationLogger.error(e.getMessage(), e);
        } catch (Throwable t) {
          incomingDTO.setHasError(true);
          object.setErrorMessage(CustomMessage.getMessageString(CommonMessage.APPLICATION_ERROR));
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

      // Rollback if atomic operation and errors occurred
    if (Boolean.TRUE.equals(incomingDTO.getHasError())) {
      if (Boolean.TRUE.equals(incomingDTO.getIsAtomicOperation())) {
        service.markRollback(incomingDTO);
        incomingDTO.setErrorMessage(
            CustomMessage.getMessageString(
                CommonMessage.ATOMIC_OPERATION_ERROR, successList.size(), errorList.size()));
      } else {
        incomingDTO.setErrorMessage(
            CustomMessage.getMessageString(
                CommonMessage.NON_ATOMIC_OPERATION_ERROR, successList.size(), errorList.size()));
      }
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
          throw new ExceptionWrapper(
              HttpStatusCode.valueOf(503),
              CommonMessage.INTERNET_IS_SLOW); // fail after max retries
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
