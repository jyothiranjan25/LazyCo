package com.example.lazyco.backend.core.CsvTemplate;

import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.File.FileTypeEnum;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class CsvService {

  public static FileDTO generateCsvFile(CsvTemplateDTO csvTemplateDTO) {
    String defaultFile =
        StringUtils.isNotEmpty(csvTemplateDTO.getCsvType())
            ? csvTemplateDTO.getCsvType()
            : "default";
    String fileName = CommonConstrains.TOMCAT_TEMP + defaultFile + FileTypeEnum.CSV.getExtension();
    File file = new File(fileName);
    try {
      try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
        if (csvTemplateDTO.getHeaders() != null) {
          String[] headers = csvTemplateDTO.getHeaders().toArray(new String[0]);
          writer.writeNext(headers);

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
