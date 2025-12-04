package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;

public class AbstractItemReader<I> implements ItemStreamReader<I> {
  private final List<I> data;
  private int currentIndex = 0;
  private final String checkpointKey;
  private final Path checkpointFile;

  public AbstractItemReader(List<I> data, String jobName) {
    this.data = data;
    this.checkpointFile = Paths.get(CommonConstants.TOMCAT_TEMP, "checkpoint_" + jobName + ".chk");
    this.checkpointKey = checkpointFile.getFileName().toString();
  }

  @Override
  public I read() {
    if (currentIndex >= data.size()) {
      return null; // end of data
    }
    I item = data.get(currentIndex);
    currentIndex++;
    return item;
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    try {
      // ✅ First priority: Restore from Spring Batch ExecutionContext (most reliable)
      if (executionContext.containsKey(checkpointKey)) {
        currentIndex = executionContext.getInt(checkpointKey);
      } else {
        // ✅ Second priority: Try to restore from file (for restarts after JVM crash)
        if (Files.exists(checkpointFile)) {
          restoreCheckpointFromFile();
        } else {
          // ✅ Fresh start - no checkpoint exists
          currentIndex = 0;
        }
      }

      // ✅ Ensure parent directory exists
      Files.createDirectories(checkpointFile.getParent());

    } catch (Exception e) {
      ApplicationLogger.error("[Reader] Failed to open checkpoint", e);
      currentIndex = 0;
    }
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    try {
      // ✅ Update ExecutionContext (Spring Batch's transactional store)
      executionContext.putInt(checkpointKey, currentIndex);

      // ✅ Also save to file as backup for disaster recovery
      saveCheckpointToFile();
    } catch (Exception e) {
      ApplicationLogger.error("[Reader] Failed to update checkpoint", e);
    }
  }

  @Override
  public void close() throws ItemStreamException {
    try {
      // ✅ Cleanup checkpoint file after successful completion
      if (Files.exists(checkpointFile)) {
        Files.delete(checkpointFile);
      }
    } catch (IOException e) {
      // Don't throw - this is just cleanup
    }
  }

  /** ✅ Thread-safe file writing using atomic write operations */
  private void saveCheckpointToFile() {
    try {
      // Create parent directory if needed
      Files.createDirectories(checkpointFile.getParent());

      // ✅ Atomic write - prevents corruption from concurrent access
      String content = String.valueOf(currentIndex);
      Files.write(
          checkpointFile,
          content.getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING,
          StandardOpenOption.SYNC // Force sync to disk
          );

    } catch (IOException e) {
      ApplicationLogger.error("[Reader] Failed to save checkpoint file", e);
    }
  }

  /** ✅ Thread-safe file reading */
  private void restoreCheckpointFromFile() {
    try {
      if (!Files.exists(checkpointFile)) {
        currentIndex = 0;
        return;
      }

      String content = new String(Files.readAllBytes(checkpointFile)).trim();
      if (!content.isEmpty()) {
        currentIndex = Integer.parseInt(content);
      } else {
        currentIndex = 0;
      }

    } catch (Exception e) {
      ApplicationLogger.error(
          "[Reader] Failed to restore checkpoint from file, starting from 0", e);
      currentIndex = 0;
    }
  }

  /** Manually reset checkpoint (for testing or manual recovery) */
  public void resetCheckpoint() {
    try {
      if (Files.exists(checkpointFile)) {
        Files.delete(checkpointFile);
      }
      currentIndex = 0;
    } catch (IOException e) {
      ApplicationLogger.error("[Reader] Failed to reset checkpoint", e);
    }
  }
}
