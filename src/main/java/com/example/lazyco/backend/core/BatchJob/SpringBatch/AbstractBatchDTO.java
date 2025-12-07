package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJobOperationType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractBatchDTO<T> extends AbstractDTO<T> {
  private BatchJobOperationType operationType;
  private Boolean sendNotification;
  private Map<Class<?>, List<?>> childDataMap = new HashMap<>();
}
