package com.example.lazyco.backend.core.CsvTemplate;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.File.FileTypeEnum;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvField;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
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
    Map<String, String> options = new HashMap<>();
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
                    CsvField annotation = f.getAnnotation(CsvField.class);
                    String[] optionsArray = annotation.options();
                    if (optionsArray.length > 0) {
                      options.put(f.getName(), String.join(",", optionsArray));
                    } else if (f.getType().isEnum()) {
                      Object[] constants = f.getType().getEnumConstants();
                      List<String> enumValues =
                          Arrays.stream(constants).map(Object::toString).toList();
                      options.put(f.getName(), String.join(",", enumValues));
                    } else if (Date.class.isAssignableFrom(f.getType())
                        || LocalDate.class.equals(f.getType())
                        || LocalDateTime.class.equals(f.getType())) {
                      options.put(f.getName(), "yyyy-MM-dd");
                    } else if (Collection.class.isAssignableFrom(f.getType())) {
                      Class<?> genericType = getCollectionGenericType(f);
                      if (genericType.isEnum()) {
                        Object[] constants = genericType.getEnumConstants();
                        List<String> enumValues =
                            Arrays.stream(constants).map(Object::toString).toList();
                        options.put(f.getName(), "[" + String.join(",", enumValues) + "]");
                      }
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
            // For each entry in optionRows, create a row with options in the correct column
            String[] optionRow = new String[csvTemplateDTO.getHeaders().size()];
            for (int i = 0; i < optionRow.length; i++) {
              String header = csvTemplateDTO.getHeaders().get(i);
              String options = csvTemplateDTO.getOptionRows().get(header);
              if (csvTemplateDTO.getHeaders().get(i).equals(header)) {
                optionRow[i] =
                    Objects.requireNonNullElse(
                        options, "Ignore This Column"); // Join options with ','
              }
            }
            writer.writeNext(optionRow);
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

  private Class<?> getCollectionGenericType(Field field) {
    try {
      var genericType = field.getGenericType();
      if (genericType instanceof java.lang.reflect.ParameterizedType parameterizedType) {
        var typeArgs = parameterizedType.getActualTypeArguments();
        if (typeArgs.length == 1) {
          var typeArg = typeArgs[0];
          if (typeArg instanceof Class<?> clazz) {
            return clazz;
          }
        }
      }
    } catch (Exception e) {
      ApplicationLogger.warn(
          "Failed to determine generic type for collection field: " + field.getName(), e);
    }
    return Object.class;
  }

  public static List<?> generateCsvToList(FileDTO file, Class<?> dtoType) {
    try {
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
          // 👇 Skip the first row after headers
          readers.skip(1);

          Map<String, String> row; // Read each subsequent row
          while ((row = readers.readMap()) != null) {
            // ✅ Skip completely empty rows
            boolean isEmptyRow =
                row.values().stream().allMatch(v -> v == null || v.trim().isEmpty());
            if (isEmptyRow) {
              continue;
            }
            rows.add(row);
          }
        }

        List<AbstractDTO> dtoList = new ArrayList<>();
        for (Map<String, String> row : rows) {
          String json = GsonSingleton.getCsvInstance().toJson(row);
          Object dto = GsonSingleton.getCsvInstance().fromJson(json, dtoType);
          dtoList.add((AbstractDTO) dto);
        }
        return dtoList;
      }
    } catch (Exception e) {
      throw new ExceptionWrapper("Failed to parse CSV into DTO: " + dtoType.getName(), e);
    }
  }
}
