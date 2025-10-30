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
  // Input fields
  private String csvType;
  private Boolean excludeOptionalFields;

  // Raw fields
  private Class<?> csvClass;
  private List<?> data;

  // Processed fields
  private List<String> headers;
  private Map<String, String> optionRows;
  private List<Map<String, String>> rows;

  // Setters with processing logic
  public void setHeaders(List<String> headers) {
    this.headers =
        headers == null
            ? null
            : headers.stream()
                .map(CsvStrategies::fieldNamingStrategy) // <-- call fieldNamingStrategy correctly
                .collect(Collectors.toList());
  }

  // Setter for optionRows with field naming strategy applied
  public void setOptionRows(Map<String, String> optionRows) {
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

  // Setter for rows with field naming strategy applied to keys
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
