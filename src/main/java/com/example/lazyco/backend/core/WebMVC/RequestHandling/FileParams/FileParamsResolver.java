package com.example.lazyco.backend.core.WebMVC.RequestHandling.FileParams;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.RequestHandlingHelper;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
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
      WebDataBinderFactory binderFactory) {
    StandardMultipartHttpServletRequest multipartRequest =
        (StandardMultipartHttpServletRequest) webRequest.getNativeRequest();

    try {
      AbstractDTO dto = RequestHandlingHelper.populateDTOFromRequest(parameter, multipartRequest);

      FileParams annotation = parameter.getParameterAnnotation(FileParams.class);
      String paramName =
          (annotation != null && StringUtils.isNotBlank(annotation.fileParam()))
              ? annotation.fileParam()
              : null;

      if (paramName == null || !multipartRequest.getFileMap().containsKey(paramName)) {
        ApplicationLogger.warn("File parameter is missing in the request");
      } else {
        boolean isMultipleFiles = annotation.isMultipleFiles();
        if (!isMultipleFiles) {
          FileDTO fileDTO =
              RequestHandlingHelper.readFileFromMultiPartRequest(paramName, multipartRequest);
          dto.setFile(fileDTO);
        } else {
          List<FileDTO> fileDTOs =
              RequestHandlingHelper.readMultipleFileFromMultiPartRequest(multipartRequest);
          dto.setFileMap(fileDTOs);
        }
      }
      return dto;
    } catch (Exception e) {
      throw new ExceptionWrapper("Failed to resolve file parameters", e);
    }
  }
}
