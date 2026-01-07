package com.example.lazyco.core.File;

import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.core.Logger.ApplicationLogger;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Getter
public class FileDTO {

  private String name;
  private String fullFileName;
  private final String absolutePath;
  private final String fileDirectory;
  private final File file;
  private final String extension;
  private final String contentType;
  private final String mimeType;
  private final Long fileSize;
  private byte[] byteArray;
  private ByteArrayOutputStream byteArrayOutputStream;

  public FileDTO(String filePath) {
    this(new File(filePath));
  }

  public FileDTO(File file) {
    if (file == null || !file.exists() || !file.isFile()) {
      throw new ExceptionWrapper("FileDTO: Invalid file: " + file);
    }
    try {
      this.file = file;
      this.name = FilenameUtils.getBaseName(file.getName());
      this.extension = FilenameUtils.getExtension(file.getName());
      this.fullFileName = file.getName();
      this.absolutePath = file.getAbsolutePath();
      this.fileDirectory = file.getParent();
      this.contentType = new Tika().detect(file);
      this.mimeType = this.contentType != null ? this.contentType.split("/")[0] : "unknown";
      this.fileSize = FileUtils.sizeOf(file);
      if (!FileTypeEnum.isSupported(this.extension)) {
        throw new ExceptionWrapper(
            "FileDTO: Unsupported file type: "
                + this.contentType
                + " for file: "
                + this.fullFileName);
      }
    } catch (ExceptionWrapper ex) {
      throw ex;
    } catch (Exception e) {
      ApplicationLogger.error(e);
      throw new ExceptionWrapper("FileDTO: Error initializing from file");
    }
  }

  public void setName(String name) {
    this.name = name;
    this.fullFileName = name + (this.extension.isEmpty() ? "" : "." + this.extension);
  }

  // Lazy load byte array
  public byte[] getByteArray() {
    if (this.byteArray == null) {
      try (FileInputStream fis = new FileInputStream(this.file)) {
        this.byteArray = fis.readAllBytes();
      } catch (IOException e) {
        ApplicationLogger.error(e);
        throw new ExceptionWrapper("FileDTO: Error reading file to byte array");
      }
    }
    return this.byteArray.clone(); // defensive copy
  }

  // Lazy load ByteArrayOutputStream
  public ByteArrayOutputStream getByteArrayOutputStream() {
    if (this.byteArrayOutputStream == null) {
      this.byteArrayOutputStream = new ByteArrayOutputStream();
      try {
        this.byteArrayOutputStream.write(getByteArray()); // use lazy-loaded byte array
      } catch (IOException e) {
        ApplicationLogger.error(e);
        throw new ExceptionWrapper("FileDTO: Error writing to ByteArrayOutputStream");
      }
    }
    return this.byteArrayOutputStream;
  }

  public Resource getResource() {
    Resource resource;
    try {
      resource = new FileSystemResource(this.file);
    } catch (Exception e) {
      ApplicationLogger.error(e);
      throw new ExceptionWrapper("FileDTO: Error getting resource");
    }
    return resource;
  }

  public FileReader getFileReader() {
    try {
      return new FileReader(this.file, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new ExceptionWrapper("FileDTO: Cannot open FileReader for " + this.absolutePath);
    }
  }

  public FileDTO moveTo(String destinationPath) {
    return moveTo(Paths.get(destinationPath));
  }

  public FileDTO moveTo(File destinationFile) {
    return moveTo(destinationFile.toPath());
  }

  public FileDTO moveTo(Path destinationPath) {
    try {
      // Ensure parent folder exists
      Path parent = destinationPath.getParent();
      if (parent != null && !Files.exists(parent)) {
        Files.createDirectories(parent);
      }

      // Move file (replace if exists)
      Files.move(this.file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
      return new FileDTO(destinationPath.toFile());
    } catch (IOException e) {
      ApplicationLogger.error(e);
      throw new ExceptionWrapper("FileDTO: Error moving file to " + destinationPath, e);
    }
  }

  public void deleteSafe() {
    try {
      if (this.file != null && this.file.exists()) {
        boolean deleted = this.file.delete();
        if (!deleted) {
          // Fallback: Apache Commons IO provides a quiet delete
          FileUtils.forceDelete(this.file);
        }
      }
    } catch (Exception e) {
      ApplicationLogger.error("Could not delete temp file: " + this.absolutePath, e);
    }
  }
}
