package com.example.lazyco.core.File.CleanupService;

import com.example.lazyco.core.Logger.ApplicationLogger;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class FileCleanupService {

  private final Path directory;
  private final int daysThreshold;

  public FileCleanupService(String directoryPath, int daysThreshold) {
    this.directory = Paths.get(directoryPath);
    this.daysThreshold = daysThreshold;
  }

  public void cleanOldFiles() {
    if (!Files.exists(directory) || !Files.isDirectory(directory)) {
      ApplicationLogger.info("Directory does not exist: " + directory);
      return;
    }

    try {
      Instant cutoff = Instant.now().minus(daysThreshold, ChronoUnit.DAYS);

      Files.walkFileTree(
          directory,
          new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException {
              Instant fileTime = attrs.lastModifiedTime().toInstant();
              if (fileTime.isBefore(cutoff)) {
                try {
                  Files.delete(file);
                  ApplicationLogger.info("Deleted file: " + file);
                } catch (IOException e) {
                  ApplicationLogger.error("Failed to delete file: " + file, e);
                }
              }
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
              if (exc != null) {
                ApplicationLogger.error("Error visiting directory: " + dir, exc);
                return FileVisitResult.CONTINUE;
              }
              if (dir.equals(directory)) return FileVisitResult.CONTINUE;
              boolean isEmpty;
              try (Stream<Path> entries = Files.list(dir)) {
                isEmpty = entries.findAny().isEmpty();
              } catch (IOException e) {
                ApplicationLogger.error("Failed to list directory: " + dir, e);
                return FileVisitResult.CONTINUE;
              }
              if (isEmpty) {
                try {
                  Files.delete(dir);
                } catch (IOException e) {
                  // Race condition: another process may have added a file — skip silently
                  ApplicationLogger.debug("Could not delete directory (may not be empty): " + dir);
                }
              }
              return FileVisitResult.CONTINUE;
            }
          });
    } catch (IOException e) {
      ApplicationLogger.error("Failed to clean up old files", e);
    }
  }
}
