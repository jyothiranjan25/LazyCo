package com.example.lazyco.backend.core.CsvTemplate;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvTemplateDTO {
  private String csvType;
  private Boolean excludeNonMandatoryFields;
  private List<String> headers;
  private Map<String, List<String>> optionRows;
  private List<Map<String, String>> rows;
}
