package com.example.lazyco.backend.core.WebMVC.RequestHandling.FileParams;

import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class FileArgumentResolver {

  public static FileDTO readFileFromMultiPartRequest(
      String paramName, MultipartHttpServletRequest multipartRequest) throws IOException {
    MultipartFile file = multipartRequest.getFile(paramName);
    if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
      return null;
    }

    // Sanitize filename to avoid path traversal
    String originalFileName = Path.of(file.getOriginalFilename()).getFileName().toString();
    String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

    // Create a temp file in a safe location
    Path tempDir = Path.of(CommonConstrains.TOMCAT_TEMP);
    if (!Files.exists(tempDir)) {
      Files.createDirectories(tempDir);
    }
    Path tempFile = tempDir.resolve(uniqueFileName);

    // Move file immediately to target location
    file.transferTo(tempFile.toFile());
    // Use streaming copy to avoid memory issues with large files
    // Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

    return new FileDTO(tempFile.toFile());
  }
}
