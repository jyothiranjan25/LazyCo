package com.example.lazyco.backend.core.File;

import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.io.*;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;

@Getter
public class FileDTO {

  private String name;
  private String fullFileName;
  private final String absolutePath;
  private final String fileDirectory;
  private final File file;
  private final String extension;
  private final String type;
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
      this.type = new Tika().detect(file);
      this.fileSize = FileUtils.sizeOf(file);
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

  public FileReader getFileReader() {
    try {
      return new FileReader(this.file);
    } catch (Exception e) {
      throw new ExceptionWrapper("FileDTO: Cannot open FileReader for " + absolutePath);
    }
  }
}
