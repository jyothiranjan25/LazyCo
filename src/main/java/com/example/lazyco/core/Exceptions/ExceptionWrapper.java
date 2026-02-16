package com.example.lazyco.core.Exceptions;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Messages.CustomMessage;
import com.example.lazyco.core.Messages.MessageCodes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ExceptionWrapper extends RuntimeException {

  @Getter private final HttpStatus httpStatus;
  @Getter private final HttpStatusCode httpStatusCode;
  @Getter private final CustomMessage customMessage;
  @Getter private Exception exception;
  @Getter private AbstractDTO<?> abstractDTO;

  public ExceptionWrapper(String message) {
    this(message, null);
  }

  public ExceptionWrapper(String message, Exception e) {
    super(message);
    this.customMessage = new CustomMessage(message);
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    this.exception = e;
  }

  public ExceptionWrapper(MessageCodes code) {
    this(code, null);
  }

  public ExceptionWrapper(MessageCodes code, Object[] args) {
    super(CustomMessage.getMessageString(code, args));
    this.customMessage =
        (ArrayUtils.isNotEmpty(args))
            ? new CustomMessage(code.getValue(), args)
            : new CustomMessage(code.getValue());
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public ExceptionWrapper(HttpStatusCode httpStatusCode, MessageCodes code) {
    this(HttpStatus.resolve(httpStatusCode.value()), code, null);
  }

  public ExceptionWrapper(HttpStatusCode httpStatusCode, MessageCodes code, Object[] args) {
    this(HttpStatus.resolve(httpStatusCode.value()), code, args);
  }

  public ExceptionWrapper(HttpStatus httpStatus, MessageCodes code) {
    this(httpStatus, code, null);
  }

  public ExceptionWrapper(HttpStatus httpStatus, MessageCodes code, Object[] args) {
    super(CustomMessage.getMessageString(code, args));
    this.customMessage =
        (ArrayUtils.isNotEmpty(args))
            ? new CustomMessage(code.getValue(), args)
            : new CustomMessage(code.getValue());
    this.httpStatus = httpStatus;
    this.httpStatusCode = httpStatus;
  }

  public ExceptionWrapper(AbstractDTO<?> abstractDTO) {
    this(HttpStatus.BAD_REQUEST, abstractDTO);
  }

  public ExceptionWrapper(HttpStatusCode httpStatusCode, AbstractDTO<?> abstractDTO) {
    this(HttpStatus.resolve(httpStatusCode.value()), abstractDTO);
  }

  public ExceptionWrapper(HttpStatus httpStatus, AbstractDTO<?> abstractDTO) {
    super();
    this.customMessage = null;
    this.httpStatus = httpStatus;
    this.httpStatusCode = httpStatus;
    this.abstractDTO = abstractDTO;
  }

  public static ExceptionWrapper getExceptionObject(MessageCodes code, Object[] args) {
    List<Object> arguments = new ArrayList<>();
    for (Object arg : args) {
      switch (arg) {
        case null -> arguments.add("NA");
        case Date date -> {
          // format the date into desired format
          SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
          String formattedDate = sdf.format(date);
          arguments.add(formattedDate);
        }
        case Collection<?> collection -> arguments.add(
            collection.stream().map(Object::toString).collect(Collectors.joining(", ", "(", ")")));
        default -> arguments.add(arg);
      }
    }
    return new ExceptionWrapper(code, arguments.toArray());
  }
}
