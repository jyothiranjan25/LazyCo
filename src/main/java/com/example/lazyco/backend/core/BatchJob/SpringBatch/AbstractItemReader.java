package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.io.*;
import java.util.List;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

public class AbstractItemReader<I> implements ItemStreamReader<I> {
  private final List<I> data;
  private int currentIndex = 0;
  private final File checkpointFile;
  private final String checkpointKey;

  public AbstractItemReader(List<I> data, String checkpointFilePath) {
    this.data = data;
    this.checkpointFile = new File(checkpointFilePath);
    this.checkpointKey = checkpointFile.getName();
    restoreCheckpoint();
  }

  @Override
  public I read() {
    ApplicationLogger.info("Current Index: " + currentIndex);
    if (currentIndex >= data.size()) {
      return null; // end of data
    }
    I item = data.get(currentIndex++);
    saveCheckpoint();
    return item;
  }

  private void saveCheckpoint() {
    try (FileWriter fw = new FileWriter(checkpointFile)) {
      fw.write(String.valueOf(currentIndex));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void restoreCheckpoint() {
    if (checkpointFile.exists()) {
      try (BufferedReader br = new BufferedReader(new FileReader(checkpointFile))) {
        String line = br.readLine();
        if (line != null && !line.isEmpty()) {
          currentIndex = Integer.parseInt(line);
        }
      } catch (Exception e) {
        currentIndex = 0;
      }
    }
  }

  public void resetCheckpoint() {
    if (checkpointFile.exists()) {
      checkpointFile.delete();
    }
    currentIndex = 0;
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    if (executionContext.containsKey(checkpointKey)) {
      currentIndex = executionContext.getInt(checkpointKey);
      ApplicationLogger.info("[Reader] Restored checkpoint from ExecutionContext: " + currentIndex);
    } else {
      restoreCheckpointFromFile();
    }
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    executionContext.putInt(checkpointKey, currentIndex);
    saveCheckpointToFile(); // Optional fallback for extra safety
    ApplicationLogger.info("[Reader] Updated checkpoint: " + currentIndex);
  }

  private void saveCheckpointToFile() {
    try (FileWriter fw = new FileWriter(checkpointFile, false)) {
      fw.write(String.valueOf(currentIndex));
    } catch (IOException e) {
      ApplicationLogger.error("[Reader] Failed to save checkpoint file", e);
    }
  }

  private void restoreCheckpointFromFile() {
    if (!checkpointFile.exists()) return;
    try (BufferedReader br = new BufferedReader(new FileReader(checkpointFile))) {
      String line = br.readLine();
      if (line != null && !line.isEmpty()) {
        currentIndex = Integer.parseInt(line.trim());
        ApplicationLogger.info("[Reader] Restored checkpoint from file: " + currentIndex);
      }
    } catch (Exception e) {
      ApplicationLogger.error("[Reader] Failed to restore checkpoint from file", e);
      currentIndex = 0;
    }
  }
}
