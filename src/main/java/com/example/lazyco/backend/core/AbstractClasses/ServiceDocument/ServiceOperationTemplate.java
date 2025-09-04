package com.example.lazyco.backend.core.AbstractClasses.ServiceDocument;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
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

    return incomingDTO;
  }

  private String resolveExceptionMessage(Throwable e) {
    while (e.getCause() != null) {
      e = e.getCause();
    }

    String defaultMessage = CustomMessage.getMessageString(CommonMessage.APPLICATION_ERROR);

    if (e instanceof org.postgresql.util.PSQLException psqlEx) {
      String detail = psqlEx.getServerErrorMessage().getDetail();
      if (detail != null) {
        defaultMessage = detail.replaceFirst("Key \\([^)]*\\)=\\((.*?)\\)", "$1");
      }
    } else if (e instanceof org.hibernate.PropertyValueException hibernateEx) {
      defaultMessage = "Field '" + hibernateEx.getPropertyName() + "' cannot be null.";
    }

    return defaultMessage;
  }

  public static <D extends AbstractDTO<D>> D executeServiceOperationTemplate(
      ServiceOperationTemplate<D> serviceOperationTemplate, D incomingDTO) {
    return serviceOperationTemplate.template(incomingDTO);
  }
}