package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class ItemException {

  private final List<ExceptionRecord> EXCEPTIONS = Collections.synchronizedList(new ArrayList<>());

  public void add(Object item, Throwable t) {
    EXCEPTIONS.add(new ExceptionRecord(item, t));
  }

  public List<ExceptionRecord> getAll() {
    return EXCEPTIONS;
  }

  @Getter
  public static class ExceptionRecord {
    private final Object item;
    private final Throwable exception;

    public ExceptionRecord(Object item, Throwable exception) {
      this.item = item;
      this.exception = exception;
    }
  }
}
