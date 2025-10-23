package com.example.lazyco.backend.core.AbstractClasses.Mapper;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.lang.reflect.Field;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NullMappingProvider {

  public static void validateAndSetNullIfFieldsAreNull(Object obj) {
    // method only works for AbstractModel
    if (!isInstanceOfAbstractModel(obj)) {
      return;
    }

    // get class and fields of the object
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();

    try {
      // for each field, check if all the inner fields are null(recursive)
      for (Field field : fields) {
        field.setAccessible(true);
        Object value = field.get(obj);

        // skip primitive types
        if (field.getType().isPrimitive()) {
          continue;
        }

        // if the field is a collection and is empty, set it to null
        if (value instanceof Collection<?> collection) {
          if (collection.isEmpty()) {
            field.set(obj, null);
          }
          continue;
        }

        // If value is null, skip
        if (value == null) {
          continue;
        }

        // If all inner fields are null, set this field to null
        if (areAllFieldsNull(value)) {
          field.set(obj, null);
        } else if (isInstanceOfAbstractModel(value)) {
          // Recursive call for nested AbstractModels
          validateAndSetNullIfFieldsAreNull(value);
        }
      }
    } catch (IllegalAccessException e) {
      ApplicationLogger.error(e, e.getClass());
    }
  }

  private static boolean isInstanceOfAbstractModel(Object obj) {
    return (obj instanceof AbstractModel);
  }

  private static boolean areAllFieldsNull(Object obj) throws IllegalAccessException {
    if (obj == null) {
      return true;
    }

    // method only works for AbstractModel
    if (!isInstanceOfAbstractModel(obj)) {
      return false;
    }

    // get class and fields of the object
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();

    // for each field, check if all the inner fields are null(recursive)
    for (Field field : fields) {
      field.setAccessible(true);
      Object value = field.get(obj);

      // skip primitive types
      if (field.getType().isPrimitive()) {
        continue;
      }

      // If collection is not empty → not all fields are null
      if (value instanceof Collection<?>) {
        if (!((Collection<?>) value).isEmpty()) {
          return false;
        } else {
          continue;
        }
      }

      if (value != null) {
        return false;
      }
    }

    return true;
  }
}
