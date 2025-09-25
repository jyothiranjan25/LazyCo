package com.example.lazyco.backend.core.AbstractClasses.Filter;

import static com.example.lazyco.backend.core.CsvTemplate.CsvStrategies.fieldNamingStrategy;
import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.OrderType;
import com.example.lazyco.backend.core.Enum.EnumService;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FilterService {

  public static List<FilterFieldMetadata> getFilterFieldMetadata(Class<?> clazz) {
    List<Field> fields = FieldFilterUtils.getAllFields(clazz);
    List<FilterFieldMetadata> filterFieldMetadata = new ArrayList<>();
    for (Field field : fields) {
      if (!field.isAnnotationPresent(FilterableField.class)) {
        continue;
      }
      FilterableField annotation = field.getAnnotation(FilterableField.class);
      FilterFieldMetadata metadata = new FilterFieldMetadata();
      metadata.setFieldName(field.getName());
      metadata.setDisplayName(
          annotation.displayName() == null || annotation.displayName().isEmpty()
              ? fieldNamingStrategy(field.getName())
              : annotation.displayName());
      metadata.setDescription(annotation.description());
      metadata.setDisplayOrder(annotation.displayOrder());
      metadata.setSortable(annotation.sortable());

      // set Expression Operations
      metadata.setSupportedExpressions(ExpressionOperation.getAllExpressionOperations());

      // set field type and enum values if applicable
      FieldType fieldType;
      String fieldClass = FilterOperator.getCollectionElementClass(field).getSimpleName();
      if (annotation.type() != null && !annotation.type().isEmpty()) {
        fieldType = FieldType.valueOf(annotation.type().toUpperCase());
        metadata.setType(fieldType);
      } else {
        fieldType = FilterOperator.getFieldType(field);
        metadata.setType(fieldType);
        if (fieldClass.equalsIgnoreCase("enum")) {
          @SuppressWarnings("unchecked")
          Class<? extends Enum<?>> enumClass =
              (Class<? extends Enum<?>>) FilterOperator.getEnumElementClass(field);
          if (enumClass != null) {
            @SuppressWarnings("unchecked")
            Map<String, String> enumMap =
                (Map<String, String>) getBean(EnumService.class).getEnumMapByType(enumClass);
            metadata.setEnumValues(enumMap);
          }
        }
      }

      // set collection element type if applicable
      metadata.setCollectionElementType(fieldClass);

      // set supported operators
      FilterOperator[] supportedOperators = FilterOperator.getDefaultOperatorsForType(fieldType);
      List<FilterFieldMetadata.FilterOption> operatorOptions =
          Arrays.stream(supportedOperators)
              .map(
                  op -> {
                    FilterFieldMetadata.FilterOption option =
                        new FilterFieldMetadata.FilterOption();
                    option.setValue(op.getOperatorName());
                    option.setDescription(op.getDisplayName());
                    option.setOperator(op);
                    return option;
                  })
              .toList();
      metadata.setSupportedOperators(operatorOptions);

      // set Order Operators
      if (annotation.sortable()) {
        metadata.setOrderTypes(OrderType.getOrderTypes());
      }

      // add metadata to list
      filterFieldMetadata.add(metadata);
    }
    return filterFieldMetadata;
  }
}
