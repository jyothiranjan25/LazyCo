package com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;

public class CsvMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {

  private static final String[] DATE_FORMATS = {
    "dd/MM/yy", // 16/02/22
    "dd/MM/yyyy", // 16/02/2022
    "MM/dd/yy", // 02/16/22 (US format)
    "MM/dd/yyyy", // 02/16/2022 (US format)
    "yyyy-MM-dd", // 2022-02-16 (ISO format)
    "dd-MM-yyyy", // 16-02-2022
    "dd-MM-yy" // 16-02-22
  };

  @Override
  protected BeanField<T, Integer> findField(int col) {
    BeanField<T, Integer> field = super.findField(col);
    if (field != null) {
      Field targetField = field.getField();

      if (targetField != null && targetField.getType().equals(Date.class)) {
        // Inject custom converter
        AbstractBeanField dateConverter =
            new AbstractBeanField<Date, String>() {
              @Override
              protected Object convert(String value) {
                if (value == null || value.isEmpty()) return null;
                try {
                    for (String fmt : DATE_FORMATS) {
                        try {
                            return Date.from(DateTimeZoneUtils.parseInstant(value, fmt));
                        } catch (Exception ignored) {}
                    }
                    return null;
                } catch (Exception e) {
                  throw new RuntimeException("Failed to parse date: " + value, e);
                }
              }
            };
        field.setField(targetField);
        field.setType(Date.class);
        return (AbstractBeanField<T, Integer>) dateConverter;
      }
    }
    return field;
  }

    public void setType(AbstractDTO dtoInstance) {
        this.setType((Class<? extends T>) dtoInstance.getClass());
    }
}
