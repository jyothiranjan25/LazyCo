package com.example.lazyco.core.CsvTemplate;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.core.File.FileDTO;
import com.example.lazyco.core.Utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CsvTemplateService {

  private final CsvService csvService;

  public CsvTemplateService(CsvService csvService) {
    this.csvService = csvService;
  }

  public ResponseEntity<?> get(CsvTemplateDTO csvTemplateDTO) {
    if (csvTemplateDTO.getCsvType() == null) {
      throw new ExceptionWrapper("Enum Type is required.");
    }
    FileDTO fileDTO = generateCsv(csvTemplateDTO);
    return ResponseUtils.sendResponse(fileDTO);
  }

  public FileDTO generateCsv(CsvTemplateDTO csvTemplateDTO) {
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
    // Set the class in the DTO
    csvTemplateDTO.setCsvClass(rawClass);

    // 3️⃣ Generate the CSV file
    return csvService.generateCsvFile(csvTemplateDTO);
  }
}
