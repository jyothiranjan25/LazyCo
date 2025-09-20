package com.example.lazyco.backend.core.CsvTemplate;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.File.FileTypeEnum;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class CsvService {

  public static FileDTO generateCsvFile(CsvTemplateDTO csvTemplateDTO) {
    String fileName =
        CommonConstrains.TOMCAT_TEMP
            + csvTemplateDTO.getCsvType()
            + FileTypeEnum.CSV.getExtension();
    File file = new File(fileName);
    try {
      try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
        if (csvTemplateDTO.getHeaders() != null) {
          String[] headers = csvTemplateDTO.getHeaders().toArray(new String[0]);
          writer.writeNext(headers);

          for (Map<String, String> row : csvTemplateDTO.getRows()) {
            String[] rowData =
                csvTemplateDTO.getHeaders().stream()
                    .map(h -> row.getOrDefault(h, "")) // default to empty if missing
                    .toArray(String[]::new);
            writer.writeNext(rowData);
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new FileDTO(file);
  }

  public static StringWriter generateCsv(List dtoList) {
    try (StringWriter writer = new StringWriter()) {
      StatefulBeanToCsv<AbstractDTO<?>> beanToCsv =
          new StatefulBeanToCsvBuilder<AbstractDTO<?>>(writer).withApplyQuotesToAll(false).build();
      beanToCsv.write(dtoList); // OpenCSV automatically maps fields
      return writer;
    } catch (Exception e) {
      throw new RuntimeException("CSV generation failed", e);
    }
  }
}
