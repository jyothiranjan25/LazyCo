package com.example.lazyco.core.BatchJob.SpringBatch;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.BatchJob.BatchJobOperationType;
import com.example.lazyco.core.BatchJob.BatchJobSessionType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractBatchDTO<T> extends AbstractDTO<T> {
  private BatchJobOperationType operationType;
  private BatchJobSessionType sessionType;
  private Boolean sendNotification;
  private Map<Class<?>, List<?>> childDataMap = new HashMap<>();
}
