package com.example.lazyco.core.AbstractClasses.Service;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Exceptions.CommonMessage;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.core.Exceptions.ResolveException;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Messages.CustomMessage;
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
    if (incomingDTO.getObjects() != null && !incomingDTO.getObjects().isEmpty()) {
      // Process all objects but track errors for atomic rollback
      for (D object : incomingDTO.getObjects()) {
        // deep clone to avoid side effects
        @SuppressWarnings("unchecked")
        D objectToProcess = (D) object.clone();
        try {
          D result = execute(objectToProcess);
          successList.add(result);
        } catch (ExceptionWrapper e) {
          ApplicationLogger.error(e.getMessage(), e);
          incomingDTO.setHasError(true);
          objectToProcess.setMessage(e.getMessage());
          errorList.add(objectToProcess);
        } catch (Exception t) {
          ApplicationLogger.error(t.getMessage(), t);
          incomingDTO.setHasError(true);
          objectToProcess.setMessage(ResolveException.resolveExceptionMessage(t));
          errorList.add(objectToProcess);
        }
      }
      resultList.addAll(successList);
      resultList.addAll(errorList);
      incomingDTO.setObjects(resultList);
    } else {
      // Single object processing
      incomingDTO = execute(incomingDTO);
    }

    // Rollback if atomic operation and errors occurred
    if (Boolean.TRUE.equals(incomingDTO.getHasError())) {
      if (Boolean.TRUE.equals(incomingDTO.getIsAtomicOperation())) {
        incomingDTO.setMessage(
            CustomMessage.getMessageString(
                CommonMessage.ATOMIC_OPERATION_ERROR, successList.size(), errorList.size()));
        service.markRollback(incomingDTO);
      } else {
        incomingDTO.setMessage(
            CustomMessage.getMessageString(
                CommonMessage.NON_ATOMIC_OPERATION_ERROR, errorList.size(), successList.size()));
      }
    }

    return incomingDTO;
  }

  public static <D extends AbstractDTO<D>> D executeServiceOperationTemplate(
      ServiceOperationTemplate<D> serviceOperationTemplate, D incomingDTO) {
    return serviceOperationTemplate.template(incomingDTO);
  }
}
