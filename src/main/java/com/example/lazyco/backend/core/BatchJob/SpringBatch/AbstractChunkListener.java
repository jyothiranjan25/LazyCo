package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class AbstractChunkListener implements ChunkListener {

  @Override
  public void beforeChunk(ChunkContext context) {
    ApplicationLogger.info("Starting Chunk in Step: " + context.getStepContext().getStepName());
  }

  @Override
  public void afterChunk(ChunkContext context) {
    ApplicationLogger.info("Finished Chunk in Step: " + context.getStepContext().getStepName());
  }

  @Override
  public void afterChunkError(ChunkContext context) {
    ApplicationLogger.info("Finished Chunk in Step: " + context.getStepContext().getStepName());
  }
}
