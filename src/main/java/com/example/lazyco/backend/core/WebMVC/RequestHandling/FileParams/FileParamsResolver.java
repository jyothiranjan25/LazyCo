package com.example.lazyco.backend.core.WebMVC.RequestHandling.FileParams;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.JSONUtils.JSONUtils;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class FileParamsResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(FileParams.class);
  }

  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {
    StandardMultipartHttpServletRequest multipartRequest =
        (StandardMultipartHttpServletRequest) webRequest.getNativeRequest();

    // Populate DTO from non-file parameters
    AbstractDTO dto = populateDTOFromRequest(parameter, multipartRequest);

    Map<String, FileDTO> filesMap = new HashMap<>();
    for (String paramName : multipartRequest.getFileMap().keySet()) {
      FileDTO attachmentFile = readFileFromMultiPartRequest(paramName, multipartRequest);
      if (attachmentFile != null
          && attachmentFile.getFile() != null
          && attachmentFile.getFile().length() > 0) {
        filesMap.put(paramName, attachmentFile);
      }
    }

    dto.setFileMap(filesMap);

    return dto;
  }

  private AbstractDTO populateDTOFromRequest(
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

  // Use this method to extract files from the request and ignore empty files
  private Map<String, FileDTO> extractFilesFromRequest(
      MultipartHttpServletRequest multipartRequest) {
    Map<String, FileDTO> filesMap = new HashMap<>();

    multipartRequest
        .getFileMap()
        .forEach(
            (paramName, multipartFile) -> {
              try {
                FileDTO fileDTO = readFileFromMultiPartRequest(paramName, multipartRequest);
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

  private FileDTO readFileFromMultiPartRequest(
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
