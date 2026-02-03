package com.example.lazyco.core.AbstractClasses.Mapper;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.Logger.ApplicationLogger;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperValidator {

  public static void validateAndSetNullIfFieldsAreNull(Object obj) {
    if (obj == null) {
      return;
    }

    if (obj instanceof AbstractDTO<?>) {
      cleanDtoObject(obj);
    } else if (obj instanceof AbstractModel) {
      cleanEntityObject(obj);
    }
  }

  private static boolean cleanDtoObject(Object obj) {
    // only process AbstractModel or AbstractDTO instances
    if (!(obj instanceof AbstractDTO<?>)) {
      return false;
    }
    Class<?> clazz = obj.getClass();
    List<Field> fields = getDeclaredFieldsRecursively(clazz);

    boolean allFieldsNull = true;

    try {
      for (Field field : fields) {
        field.setAccessible(true);
        Object value = field.get(obj);

        // direct null check
        if (value == null) {
          continue;
        }

        // blank string check
        if (value instanceof String str) {
          if (str.isBlank()) {
            field.set(obj, null);
          } else {
            allFieldsNull = false;
          }
          continue;
        }

        // collection check
        if (value instanceof Collection<?> collection) {
          if (collection.isEmpty()) {
            field.set(obj, null);
          } else {
            for (Object item : collection) {
              if (item != null) {
                cleanDtoObject(item);
              }
            }
            allFieldsNull = false;
          }
          continue;
        }

        // nested DTO / Model
        if (value instanceof AbstractModel || value instanceof AbstractDTO<?>) {
          boolean innerAllNull = cleanDtoObject(value);
          if (innerAllNull) {
            field.set(obj, null);
          } else {
            allFieldsNull = false;
          }
          continue;
        }

        // any other non-null value
        allFieldsNull = false;
      }
    } catch (IllegalAccessException e) {
      ApplicationLogger.error(e, e.getClass());
      return false;
    }

    return allFieldsNull;
  }

  private static boolean cleanEntityObject(Object obj) {
    if (!isInstanceOfAbstractModel(obj)) {
      return true;
    }

    Class<?> clazz = obj.getClass();
    List<Field> fields = getDeclaredFieldsRecursively(clazz);

    boolean allFieldsNull = true;

    try {
      for (Field field : fields) {
        field.setAccessible(true);
        Object value = field.get(obj);

        // skip primitive types and collections
        if (field.getType().isPrimitive() || (value instanceof Collection)) {
          continue;
        }

        // null value → nothing to clean
        if (value == null) {
          continue;
        }

        // nested entity
        if (isInstanceOfAbstractModel(value)) {
          boolean innerAllNull = cleanEntityObject(value);
          if (innerAllNull) {
            field.set(obj, null);
          } else {
            allFieldsNull = false;
          }
          continue;
        }

        // any other non-null value
        allFieldsNull = false;
      }

    } catch (IllegalAccessException e) {
      ApplicationLogger.error(e);
      return false;
    }

    return allFieldsNull;
  }

  private static boolean isInstanceOfAbstractModel(Object obj) {
    return (obj instanceof AbstractModel);
  }

  // Additional helper methods can be added here as needed
  private static List<Field> getDeclaredFieldsRecursively(Class<?> clazz) {
    if (clazz == null || clazz.equals(Object.class)) {
      return List.of();
    }

    if (clazz == AbstractDTO.class || clazz == AbstractModel.class) {
      return List.of();
    }

    List<Field> fields = new ArrayList<>(List.of(clazz.getDeclaredFields()));

    Class<?> superClass = clazz.getSuperclass();

    if (superClass != null && superClass != AbstractDTO.class) {
      fields.addAll(getDeclaredFieldsRecursively(superClass));
    }

    // Explicitly add "id" field from AbstractModel
    try {
      Field idField;
      if (AbstractDTO.class.isAssignableFrom(clazz)) {
        idField = AbstractDTO.class.getDeclaredField("id");
      } else if (AbstractModel.class.isAssignableFrom(clazz)) {
        idField = AbstractModel.class.getDeclaredField("id");
      } else {
        return fields;
      }
      idField.setAccessible(true);

      // avoid duplicates
      boolean alreadyPresent = fields.stream().anyMatch(f -> f.getName().equals(idField.getName()));

      if (!alreadyPresent) {
        fields.add(idField);
      }
    } catch (NoSuchFieldException ignored) {
      // id does not exist — safe to ignore
    }

    return fields;
  }
}
