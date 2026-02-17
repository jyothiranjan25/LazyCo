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

  public abstract D execute(D incomingRequest);

  public D template(D incomingRequest) {
    List<D> resultList = new ArrayList<>();
    List<D> successList = new ArrayList<>();
    List<D> errorList = new ArrayList<>();
    if (incomingRequest.getObjects() != null && !incomingRequest.getObjects().isEmpty()) {
      // Process all objects but track errors for atomic rollback
      for (D object : incomingRequest.getObjects()) {
        // deep clone to avoid side effects
        @SuppressWarnings("unchecked")
        D objectToProcess = (D) object.clone();
        try {
          D result = execute(objectToProcess);
          successList.add(result);
        } catch (ExceptionWrapper e) {
          ApplicationLogger.error(e.getMessage(), e);
          incomingRequest.setHasError(true);
          objectToProcess.setMessage(e.getMessage());
          errorList.add(objectToProcess);
        } catch (Exception t) {
          ApplicationLogger.error(t.getMessage(), t);
          incomingRequest.setHasError(true);
          objectToProcess.setMessage(ResolveException.resolveExceptionMessage(t));
          errorList.add(objectToProcess);
        }
      }
      resultList.addAll(successList);
      resultList.addAll(errorList);
      incomingRequest.setObjects(resultList);
    } else {
      // Single object processing
      incomingRequest = execute(incomingRequest);
    }

    // Rollback if atomic operation and errors occurred
    if (Boolean.TRUE.equals(incomingRequest.getHasError())) {
      if (Boolean.TRUE.equals(incomingRequest.getIsAtomicOperation())) {
        incomingRequest.setMessage(
            CustomMessage.getMessageString(
                CommonMessage.ATOMIC_OPERATION_ERROR, successList.size(), errorList.size()));
        service.markRollback(incomingRequest);
      } else {
        incomingRequest.setMessage(
            CustomMessage.getMessageString(
                CommonMessage.NON_ATOMIC_OPERATION_ERROR, errorList.size(), successList.size()));
      }
    }

    return incomingRequest;
  }

  public static <D extends AbstractDTO<D>> D executeServiceOperationTemplate(
      ServiceOperationTemplate<D> serviceOperationTemplate, D incomingRequest) {
    return serviceOperationTemplate.template(incomingRequest);
  }
}
