package com.example.lazyco.backend.core.AbstractClasses.Mapper;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Collection;

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
                if (field.getType().isPrimitive()
                        || field.getName().equals("objectName")
                        || (value != null && value instanceof Collection)) {
                    continue;
                }
                // set the value of curent field to null in the current object if all the inner fields(of
                // the current field) are null
                if (areAllFieldsNull(value)) {
                    field.set(obj, null);
                } else {
                    // if the current field is an instance of AbstractModel,
                    // then call the same method on the current field(to nullify all its empty fields)
                    if (isInstanceOfAbstractModel(value)) {
                        validateAndSetNullIfFieldsAreNull(value);
                    }
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
        if (!isInstanceOfAbstractModel(obj)) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            // skip primitive types
            if (field.getType().isPrimitive()
                    || field.getName().equals("objectName")
                    || (value instanceof Collection)) {
                continue;
            }

            if (value != null) {
                return false;
            }
        }

        return true;
    }
}
