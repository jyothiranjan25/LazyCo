package com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.File.FileTypeEnum;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.FileParams.FileArgumentResolver;
import com.opencsv.CSVReaderHeaderAware;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class CsvParamsResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CsvParams.class);
  }

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
    String paramName =
        (annotation != null && StringUtils.isNotBlank(annotation.fileParam()))
            ? annotation.fileParam()
            : null;

    if (paramName == null || !multipartRequest.getFileMap().containsKey(paramName)) {
      throw new ExceptionWrapper("File parameter is missing in the request");
    }
    FileDTO file = FileArgumentResolver.readFileFromMultiPartRequest(paramName, multipartRequest);
    if (file == null || file.getFile() == null || file.getFile().length() == 0) {
      throw new ExceptionWrapper("Uploaded file is empty or invalid");
    }
    if (!Objects.equals(file.getExtension(), FileTypeEnum.CSV.name().toLowerCase())) {
      throw new ExceptionWrapper("Uploaded file is not a CSV file");
    }

    Class<?> dtoType = parameter.getParameterType();
    try (InputStream fis = new FileInputStream(file.getFile());
        BOMInputStream bomInputStream =
            BOMInputStream.builder()
                .setInputStream(fis)
                .setByteOrderMarks(
                    ByteOrderMark.UTF_8,
                    ByteOrderMark.UTF_16LE,
                    ByteOrderMark.UTF_16BE,
                    ByteOrderMark.UTF_32LE,
                    ByteOrderMark.UTF_32BE)
                .setInclude(false)
                .get();
        Reader reader = new InputStreamReader(bomInputStream, StandardCharsets.UTF_8)) {

      List<Map<String, String>> rows = new ArrayList<>();
      try (CSVReaderHeaderAware readers = new CSVReaderHeaderAware(reader)) {
        Map<String, String> row;
        while ((row = readers.readMap()) != null) {
          rows.add(row);
        }
      }

      AbstractDTO dtoInstance = (AbstractDTO) dtoType.getDeclaredConstructor().newInstance();
      List<AbstractDTO> dtoList = new ArrayList<>();
      for (Map<String, String> row : rows) {
        String json = GsonSingleton.getCsvInstance().toJson(row);
        dtoList.add(
            GsonSingleton.getCsvInstance()
                .fromJson(json, (Class<? extends AbstractDTO>) parameter.getParameterType()));
      }
      dtoInstance.setObjects(dtoList);
      return dtoInstance;
    } catch (Exception e) {
      ApplicationLogger.error("Failed to parse CSV into DTO: " + dtoType.getName(), e);
      throw e;
    } finally {
      file.deleteSafe();
    }
  }
}
