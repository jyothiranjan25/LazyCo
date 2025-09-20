package com.example.lazyco.backend.core.CsvTemplate;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvField;
import com.example.lazyco.backend.core.WebMVC.SpringContext;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserService;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CsvTemplateService {

  public ResponseEntity<?> get(CsvTemplateDTO csvTemplateDTO) {
    if (csvTemplateDTO.getCsvType() == null) {
      throw new ExceptionWrapper("Enum Type is required.");
    }
    // 1️⃣ Look up the enum and class
    Class<?> rawClass;
    try {
      CsvClasses csvClasses = CsvClasses.getByKey(csvTemplateDTO.getCsvType());
      rawClass = Class.forName(csvClasses.getCsvDTOClass().getName());
    } catch (Exception e) {
      throw new ExceptionWrapper("Enum class " + csvTemplateDTO.getCsvType() + " is not found");
    }
    // 2️⃣ Verify it extends AbstractDTO
    if (!AbstractDTO.class.isAssignableFrom(rawClass)) {
      throw new ExceptionWrapper("Provided class is not a valid CSV DTO.");
    }
    @SuppressWarnings("unchecked")
    Class<? extends AbstractDTO<?>> csvClass = (Class<? extends AbstractDTO<?>>) rawClass;

    // 3️⃣ Extract fields with @CsvField annotation and sort by order
    List<String> headers =
        Arrays.stream(rawClass.getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(CsvField.class))
            .filter(
                f -> {
                  if (!Boolean.TRUE.equals(csvTemplateDTO.getExcludeNonMandatoryFields())) {
                    return true; // keep all fields
                  }
                  CsvField annotation = f.getAnnotation(CsvField.class);
                  return !annotation.optional(); // exclude optional fields
                })
            .sorted(
                (f1, f2) -> {
                  int o1 = f1.getAnnotation(CsvField.class).order();
                  int o2 = f2.getAnnotation(CsvField.class).order();
                  return Integer.compare(o1, o2);
                })
            .map(new GsonSingleton.CsvFieldNamingStrategy()::translateName)
            .toList();
    List<AppUserDTO> appUserDTOS =
        SpringContext.getBean(AppUserService.class).get(new AppUserDTO());
    List rows = CsvObjectMapper.csvMapper(appUserDTOS);
    csvTemplateDTO.setHeaders(headers);
    csvTemplateDTO.setRows(rows);
    FileDTO fileDTO = CsvService.generateCsvFile(csvTemplateDTO);

    return ResponseUtils.sendResponse(fileDTO);
  }
}
