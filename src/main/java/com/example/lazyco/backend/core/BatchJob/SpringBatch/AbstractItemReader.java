package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import java.io.*;
import java.util.List;
import org.springframework.batch.item.ItemReader;

public class AbstractItemReader<I> implements ItemReader<I> {
  private final List<I> data;
  private int currentIndex = 0;
  private final File checkpointFile;

  public AbstractItemReader(List<I> data, String checkpointFilePath) {
    this.data = data;
    this.checkpointFile = new File(checkpointFilePath);
    restoreCheckpoint();
  }

  @Override
  public I read() {
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
}
