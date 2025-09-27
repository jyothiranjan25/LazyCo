package com.example.lazyco.backend.core.CsvTemplate;

import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.File.FileTypeEnum;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvField;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CsvService {

  public CsvTemplateDTO generateCsvHeaders(CsvTemplateDTO csvDto) {
    // Clone the input DTO to avoid mutating it
    CsvTemplateDTO csvTemplateDTO = csvDto.clone();
    Class<?> rawClass = csvTemplateDTO.getCsvClass();
    if (rawClass == null) {
      throw new IllegalArgumentException("CSV class must be set before generating headers.");
    }
    // Prepare to collect headers and options
    Map<String, List<String>> options = new HashMap<>();
    List<String> headers =
        Arrays.stream(rawClass.getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(CsvField.class))
            .filter(
                f -> {
                  if (!Boolean.TRUE.equals(csvTemplateDTO.getExcludeOptionalFields())) {
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
            .peek(
                f -> {
                  try {
                    if (f.getType().isEnum()) {
                      Object[] constants = f.getType().getEnumConstants();
                      List<String> enumValues =
                          Arrays.stream(constants).map(Object::toString).toList();
                      options.put(f.getName(), enumValues);
                    }
                  } catch (Exception e) {
                    ApplicationLogger.warn("Failed to process enum field: " + f.getName(), e);
                  }
                })
            .map(Field::getName)
            .toList();
    // Set headers and options in the clone
    csvTemplateDTO.setHeaders(headers);
    csvTemplateDTO.setOptionRows(options);
    return csvTemplateDTO;
  }

  // Convert list of objects to list of maps for CSV rows
  @SuppressWarnings("rawtypes")
  public List generateCsvRows(List<?> objects) {
    if (objects == null || objects.isEmpty()) {
      return List.of();
    }
    return GsonSingleton.getCsvInstance()
        .fromJson(GsonSingleton.getCsvInstance().toJson(objects), List.class);
  }

  public FileDTO generateCsvFile(CsvTemplateDTO csvTemplateDTO) {
    String defaultFile =
        StringUtils.isNotEmpty(csvTemplateDTO.getCsvType())
            ? csvTemplateDTO.getCsvType()
            : "default";
    String fileName = CommonConstrains.TOMCAT_TEMP + defaultFile + FileTypeEnum.CSV.getExtension();
    File file = new File(fileName);
    try {
      try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {

        // generate headers if not set
        if (csvTemplateDTO.getHeaders() == null || csvTemplateDTO.getHeaders().isEmpty()) {
          csvTemplateDTO = generateCsvHeaders(csvTemplateDTO);
        }

        // Write BOM for UTF-8 encoding
        if (csvTemplateDTO.getHeaders() != null) {
          // Write headers
          String[] headers = csvTemplateDTO.getHeaders().toArray(new String[0]);
          writer.writeNext(headers);

          // Write option rows if available
          if (csvTemplateDTO.getOptionRows() != null && !csvTemplateDTO.getOptionRows().isEmpty()) {
            for (Map.Entry<String, List<String>> entry :
                csvTemplateDTO.getOptionRows().entrySet()) {
              String header = entry.getKey();
              List<String> options = entry.getValue();
              String optionsStr = String.join(",", options); // Join options with ','
              String[] optionRow = new String[csvTemplateDTO.getHeaders().size()];
              for (int i = 0; i < optionRow.length; i++) {
                if (csvTemplateDTO.getHeaders().get(i).equals(header)) {
                  optionRow[i] = optionsStr;
                } else {
                  optionRow[i] = ""; // Empty for other columns
                }
              }
              writer.writeNext(optionRow);
            }
          }

          // Generate rows from data if provided
          if (csvTemplateDTO.getData() != null && !csvTemplateDTO.getData().isEmpty()) {
            csvTemplateDTO.setRows(generateCsvRows(csvTemplateDTO.getData()));
          }

          // Write data rows
          if (csvTemplateDTO.getRows() != null && !csvTemplateDTO.getRows().isEmpty()) {
            for (Map<String, String> row : csvTemplateDTO.getRows()) {
              String[] rowData =
                  csvTemplateDTO.getHeaders().stream()
                      .map(h -> row.getOrDefault(h, "")) // default to empty if missing
                      .toArray(String[]::new);
              writer.writeNext(rowData);
            }
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new FileDTO(file);
  }
}
