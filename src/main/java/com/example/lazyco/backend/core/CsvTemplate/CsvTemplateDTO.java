package com.example.lazyco.backend.core.CsvTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

@Getter
@Setter
public class CsvTemplateDTO implements Serializable, Cloneable {
  private String csvType;
  private Boolean excludeNonMandatoryFields;
  private Class<?> csvClass;
  private List<?> data;
  private List<String> headers;
  private Map<String, List<String>> optionRows;
  private List<Map<String, String>> rows;

  public void setHeaders(List<String> headers) {
    this.headers =
        headers == null
            ? null
            : headers.stream()
                .map(CsvStrategies::fieldNamingStrategy) // <-- call fieldNamingStrategy correctly
                .collect(Collectors.toList());
  }

  public void setOptionRows(Map<String, List<String>> optionRows) {
    this.optionRows =
        optionRows == null
            ? null
            : optionRows.entrySet().stream()
                .collect(
                    Collectors.toMap(
                        e ->
                            CsvStrategies.fieldNamingStrategy(
                                e.getKey()), // <-- call fieldNamingStrategy correctly
                        Map.Entry::getValue));
  }

  public void setRows(List<Map<String, String>> rows) {
    this.rows =
        rows == null
            ? null
            : rows.stream()
                .map(
                    row ->
                        row.entrySet().stream()
                            .collect(
                                Collectors.toMap(
                                    e ->
                                        CsvStrategies.fieldNamingStrategy(
                                            e.getKey()), // <-- call fieldNamingStrategy correctly
                                    Map.Entry::getValue)))
                .collect(Collectors.toList());
  }

  @Override
  public CsvTemplateDTO clone() {
    CsvTemplateDTO clone = null;
    try {
      clone = (CsvTemplateDTO) super.clone();

      return SerializationUtils.clone(clone);
    } catch (CloneNotSupportedException e) {
      return clone;
    }
  }
}
