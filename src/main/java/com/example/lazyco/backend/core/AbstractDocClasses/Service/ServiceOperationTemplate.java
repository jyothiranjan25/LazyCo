package com.example.lazyco.backend.core.AbstractDocClasses.Service;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.CommonMessage;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Messages.CustomMessage;
import java.util.ArrayList;
import java.util.List;

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
        // deep clone to avoid side effects
        D objectToProcess = (D) object.clone();
        try {
          D result = execute(objectToProcess);
          successList.add(result);
        } catch (ExceptionWrapper e) {
          incomingDTO.setHasError(true);
          objectToProcess.setErrorMessage(e.getMessage());
          errorList.add(objectToProcess);
          ApplicationLogger.error(e.getMessage(), e);
        } catch (Throwable t) {
          incomingDTO.setHasError(true);
          objectToProcess.setErrorMessage(resolveExceptionMessage(t));
          errorList.add(objectToProcess);
          ApplicationLogger.error(t.getMessage(), t);
        }
      }
      resultList.addAll(successList);
      resultList.addAll(errorList);
      incomingDTO.setObjectsList(resultList);
    } else {
      // Single object processing
      incomingDTO = execute(incomingDTO);
    }

    // Rollback if atomic operation and errors occurred
    if (Boolean.TRUE.equals(incomingDTO.getHasError())) {
      if (Boolean.TRUE.equals(incomingDTO.getIsAtomicOperation())) {
        incomingDTO.setErrorMessage(
            CustomMessage.getMessageString(
                CommonMessage.ATOMIC_OPERATION_ERROR, successList.size(), errorList.size()));
        service.markRollback(incomingDTO);
      } else {
        incomingDTO.setErrorMessage(
            CustomMessage.getMessageString(
                CommonMessage.NON_ATOMIC_OPERATION_ERROR, errorList.size(), successList.size()));
      }
    }

    return incomingDTO;
  }

  private String resolveExceptionMessage(Throwable e) {
    while (e.getCause() != null) {
      e = e.getCause();
    }
    return CustomMessage.getMessageString(CommonMessage.APPLICATION_ERROR);
  }

  public static <D extends AbstractDTO<D>> D executeServiceOperationTemplate(
      ServiceOperationTemplate<D> serviceOperationTemplate, D incomingDTO) {
    return serviceOperationTemplate.template(incomingDTO);
  }
}
