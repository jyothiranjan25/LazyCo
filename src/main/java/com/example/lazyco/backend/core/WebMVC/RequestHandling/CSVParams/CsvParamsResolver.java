package com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams;

import static com.example.lazyco.backend.core.CsvTemplate.CsvService.generateCsvToList;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.File.FileTypeEnum;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.RequestHandlingHelper;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component
@SuppressWarnings({"rawtypes", "unchecked"})
public class CsvParamsResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CsvParams.class);
  }

  /**
   * Resolves method arguments annotated with @CsvParams by extracting the CSV file and populating
   * the corresponding DTO from the multipart request.
   */
  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {

    if (!(webRequest.getNativeRequest() instanceof MultipartHttpServletRequest multipartRequest)) {
      throw new ExceptionWrapper("Request is not a multipart request");
    }

    CsvParams annotation = parameter.getParameterAnnotation(CsvParams.class);
    boolean dtoAsFileParam = annotation != null && annotation.dtoAsFileParam();

    String paramName;
    if (dtoAsFileParam) {
      // Use DTO class name as file parameter
      paramName = parameter.getParameterType().getSimpleName();
    } else {
      paramName =
          (annotation != null && StringUtils.isNotBlank(annotation.fileParam()))
              ? annotation.fileParam()
              : null;
    }

    if (paramName == null || !multipartRequest.getFileMap().containsKey(paramName)) {
      throw new ExceptionWrapper("File parameter is missing in the request");
    }

    FileDTO file = RequestHandlingHelper.readSingleFileFromRequest(paramName, multipartRequest);
    if (file == null || file.getFile() == null || file.getFile().length() == 0) {
      throw new ExceptionWrapper("Uploaded file is empty or invalid");
    }
    if (!Objects.equals(file.getExtension(), FileTypeEnum.CSV.name().toLowerCase())) {
      throw new ExceptionWrapper("Uploaded file is not a CSV file");
    }
    try {
      AbstractDTO dtoInstance =
          RequestHandlingHelper.populateDTOFromRequest(parameter, multipartRequest);
      dtoInstance.setFile(file);
      dtoInstance.setObjects(generateCsvToList(file, dtoInstance.getClass()));
      return dtoInstance;
    } catch (Exception e) {
      throw new ExceptionWrapper("Failed to parse CSV", e);
    }
  }
}
