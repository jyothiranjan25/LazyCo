package com.example.lazyco.backend.core.WebMVC.RequestHandling.FileParams;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.RequestHandlingHelper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
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

    AbstractDTO dto = RequestHandlingHelper.populateDTOFromRequest(parameter, multipartRequest);
    dto.setFileMap(extractFilesFromRequest(multipartRequest));
    return dto;
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
                FileDTO fileDTO = RequestHandlingHelper.readFileFromMultiPartRequest(multipartFile);
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
}
