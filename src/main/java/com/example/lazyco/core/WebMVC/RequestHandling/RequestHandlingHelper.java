package com.example.lazyco.core.WebMVC.RequestHandling;

import com.example.lazyco.core.File.FileDTO;
import com.example.lazyco.core.GosnConf.GsonSingleton;
import com.example.lazyco.core.JSONUtils.JSONUtils;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.CommonConstants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class RequestHandlingHelper {

  // Use this method to populate DTO from parameters
  @SuppressWarnings({"unchecked"})
  public static <T> T populateDTOFromRequest(MethodParameter mp, MultipartHttpServletRequest mr) {

    JSONObject jsonObject = new JSONObject();
    mr.getParameterMap()
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
    return (T)
        GsonSingleton.getInstance()
            .fromJson(jsonObject.toString(), (Class<?>) mp.getParameterType());
  }

  // Read single file from multipart request by parameter name
  public static FileDTO readSingleFileFromRequest(
      String paramName, MultipartHttpServletRequest multipartRequest) {
    return saveFileToTemp(multipartRequest.getFile(paramName));
  }

  // Read all files from multipart request
  public static List<FileDTO> readMultipleFileFromMultiPartRequest(
      String paramName, MultipartHttpServletRequest multipartRequest) {
    return multipartRequest.getFiles(paramName).stream()
        .map(
            multipartFile -> {
              try {
                return RequestHandlingHelper.saveFileToTemp(multipartFile);
              } catch (Exception e) {
                ApplicationLogger.error(
                    "Error processing file: " + multipartFile.getOriginalFilename(), e);
                return null;
              }
            })
        .filter(Objects::nonNull)
        .filter(dto -> dto.getFile() != null && dto.getFile().length() > 0)
        .collect(Collectors.toList());
  }

  // Use this method to extract files from the request and ignore empty files
  public static Map<String, FileDTO> extractFileFromRequest(
      MultipartHttpServletRequest multipartRequest) {
    Map<String, FileDTO> filesMap = new HashMap<>();
    multipartRequest
        .getFileMap()
        .forEach(
            (paramName, multipartFile) -> {
              try {
                FileDTO fileDTO = readSingleFileFromRequest(paramName, multipartRequest);
                if (fileDTO != null
                    && fileDTO.getFile() != null
                    && fileDTO.getFile().length() > 0) {
                  filesMap.put(paramName, fileDTO);
                }
              } catch (Exception e) {
                ApplicationLogger.error("Error processing file parameter: " + paramName, e);
              }
            });
    return filesMap;
  }

  // Use this method to extract files from the request and ignore empty files
  public static Map<String, List<FileDTO>> extractFilesFromRequest(
      MultipartHttpServletRequest multipartRequest) {
    Map<String, List<FileDTO>> filesMap = new HashMap<>();
    multipartRequest
        .getFileMap()
        .forEach(
            (paramName, multipartFile) -> {
              try {
                List<FileDTO> fileDTOs =
                    readMultipleFileFromMultiPartRequest(paramName, multipartRequest);
                if (!fileDTOs.isEmpty()) {
                  filesMap.put(paramName, fileDTOs);
                }
              } catch (Exception e) {
                ApplicationLogger.error("Error processing file parameter: " + paramName, e);
              }
            });

    return filesMap;
  }

  // Read single file, validate, and save to a safe temp directory
  public static FileDTO saveFileToTemp(MultipartFile file) {
    if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
      return null;
    }

    // Sanitize filename to avoid path traversal
    String originalFileName = Path.of(file.getOriginalFilename()).getFileName().toString();
    String uniqueFileName = UUID.randomUUID().toString().concat("_").concat(originalFileName);

    // Create a temp file in a safe location
    Path tempDir = Path.of(CommonConstants.TOMCAT_TEMP);
    try {
      if (!Files.exists(tempDir)) {
        Files.createDirectories(tempDir);
      }
    } catch (IOException e) {
      ApplicationLogger.error("Failed to create temp directory: " + tempDir, e);
      return null;
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
      ApplicationLogger.error("Failed to transfer fileto temp location: " + tempFileCheck, e);
      // If transferTo fails, fallback to streaming copy (safe for large files)
      try (InputStream inputStream = file.getInputStream()) {
        // Use streaming copy to avoid memory issues with large files
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException copyEx) {
        ApplicationLogger.error(
            "Failed to copy uploaded file to temp location: " + tempFileCheck, copyEx);
        try {
          // Cleanup partially written file
          Files.deleteIfExists(tempFile);
        } catch (IOException deleteEx) {
          ApplicationLogger.error("Failed to delete partial temp file: " + tempFile, deleteEx);
        }
        return null;
      }
    }

    return new FileDTO(tempFileCheck);
  }
}
