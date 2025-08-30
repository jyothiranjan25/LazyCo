package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.CustomMessage;
import com.example.lazyco.backend.core.Messages.MessageCodes;
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

  public ExceptionWrapper(String message) {
    super(message);
    this.customMessage = new CustomMessage(message);
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
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

  public ExceptionWrapper(HttpStatusCode httpStatus, MessageCodes code) {
    this(httpStatus, code, null);
  }

  public ExceptionWrapper(HttpStatusCode httpStatus, MessageCodes code, Object[] args) {
    super(CustomMessage.getMessageString(code, args));
    this.customMessage =
        (ArrayUtils.isNotEmpty(args))
            ? new CustomMessage(code.getValue(), args)
            : new CustomMessage(code.getValue());
    this.httpStatus = HttpStatus.resolve(httpStatus.value());
    this.httpStatusCode = httpStatus;
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
