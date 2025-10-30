package com.example.lazyco.backend.core.WebMVC.RequestHandling;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.JSONUtils.JSONUtils;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class RequestHandlingHelper {

  // Use this method to populate DTO from non-file parameters
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static AbstractDTO populateDTOFromRequest(
      MethodParameter parameter, MultipartHttpServletRequest multipartRequest) {

    JSONObject jsonObject = new JSONObject();
    multipartRequest
        .getParameterMap()
        .forEach(
            (paramName, paramValues) -> {
              if (StringUtils.isBlank(paramName)) return; // skip blank parameter names
              String paramValue = ArrayUtils.isNotEmpty(paramValues) ? paramValues[0] : null;
              if (paramValue != null) {
                paramValue = paramValue.trim(); // trim whitespace
                if (paramValue.isEmpty()) paramValue = null; // convert empty strings to null
              }
              JSONUtils.addParameterToJson(jsonObject, paramName, paramValue);
            });

    // Convert JSON to DTO
    return GsonSingleton.getInstance()
        .fromJson(
            jsonObject.toString(), (Class<? extends AbstractDTO>) parameter.getParameterType());
  }

  // Read file from multipart request by parameter name
  public static FileDTO readFileFromMultiPartRequest(
      String paramName, MultipartHttpServletRequest multipartRequest) throws IOException {
    MultipartFile file = multipartRequest.getFile(paramName);
    if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
      return null;
    }
    return readFileFromMultiPartRequest(file);
  }

  // Read file from multipart request and save to temp location
  public static FileDTO readFileFromMultiPartRequest(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
      return null;
    }

    // Sanitize filename to avoid path traversal
    String originalFileName = Path.of(file.getOriginalFilename()).getFileName().toString();
    String uniqueFileName = UUID.randomUUID().toString().concat("_").concat(originalFileName);

    // Create a temp file in a safe location
    Path tempDir = Path.of(CommonConstrains.TOMCAT_TEMP);
    if (!Files.exists(tempDir)) {
      Files.createDirectories(tempDir);
    }

    // Ensure unique file path
    Path tempFile = tempDir.resolve(uniqueFileName);

    // Create the file
    File tempFileCheck = tempFile.toFile();

    try {
      // First try transferTo (fastest, platform-dependent)
      // Move file immediately to target location
      file.transferTo(tempFileCheck);
    } catch (Exception e) {
      // If transferTo fails, fallback to streaming copy (safe for large files)
      try (InputStream inputStream = file.getInputStream()) {
        // Use streaming copy to avoid memory issues with large files
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException copyEx) {
        ApplicationLogger.error("Failed to copy uploaded file to temp location", copyEx);
        // Cleanup partially written file
        Files.deleteIfExists(tempFile);
        throw copyEx; // rethrow to caller
      }
    }

    return new FileDTO(tempFileCheck);
  }
}
