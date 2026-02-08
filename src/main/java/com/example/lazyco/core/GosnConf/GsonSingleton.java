package com.example.lazyco.core.GosnConf;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

import com.example.lazyco.core.CsvTemplate.CsvStrategies;
import com.example.lazyco.core.DateUtils.DateParser;
import com.example.lazyco.core.DateUtils.DateTimeProps;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.WebMVC.RequestHandling.CSVParams.CsvField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonSingleton {
  private static volatile Gson instance;

  public static Gson getInstance() {
    if (instance == null) {
      synchronized (GsonSingleton.class) {
        if (instance == null) {
          // Create Gson instance with custom serializer
          GsonBuilder gsonBuilder = new GsonBuilder();

          // Register custom serializers
          gsonBuilder.setPrettyPrinting();
          //          gsonBuilder.serializeNulls();
          gsonBuilder.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES);
          gsonBuilder.addSerializationExclusionStrategy(new SerializationExclusionStrategy());
          gsonBuilder.addDeserializationExclusionStrategy(new DeserializationExclusionStrategy());

          registerTypeAdapter(gsonBuilder);

          instance = gsonBuilder.create();
        }
      }
    }
    return instance;
  }

  private static void registerTypeAdapter(GsonBuilder gsonBuilder) {
    gsonBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
    gsonBuilder.registerTypeAdapter(Time.class, new TimeTypeAdapter());
    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
    gsonBuilder.registerTypeAdapter(String.class, new StringTypeAdapter());
    gsonBuilder.serializeSpecialFloatingPointValues();
    gsonBuilder.registerTypeAdapter(Number.class, new NumberTypeAdapter());
    gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
    gsonBuilder.registerTypeAdapter(Class.class, new ClassTypeAdapter());
  }

  // this instance is used for CSV parsing where we want to ignore nulls
  public static Gson getCsvInstance() {
    // Create Gson instance with custom serializer
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setPrettyPrinting();
    registerTypeAdapter(gsonBuilder);
    gsonBuilder.addSerializationExclusionStrategy(new CsvExclusionStrategy());
    gsonBuilder.addDeserializationExclusionStrategy(new CsvExclusionStrategy());
    gsonBuilder.setFieldNamingStrategy(new CsvFieldNamingStrategy());
    gsonBuilder.registerTypeAdapterFactory(new CsvTypeAdapterFactory());
    return gsonBuilder.create();
  }

  public static Gson getGson() {
    // Create Gson instance with custom serializer
    GsonBuilder gsonBuilder = new GsonBuilder();
    // Register custom serializers
    gsonBuilder.setPrettyPrinting();
    registerTypeAdapter(gsonBuilder);
    gsonBuilder.registerTypeAdapterFactory(new LenientTypeAdapterFactory());
    return gsonBuilder.create();
  }

  public static String convertObjectToJSONString(Object o) {
    return getInstance().toJson(o);
  }

  public static JSONObject convertObjectToJSONObject(Object o) {
    return new JSONObject(convertObjectToJSONString(o));
  }

  public static <T> T convertJSONStringToObject(String jsonString, Class<T> classOfT) {
    return getInstance().fromJson(jsonString, classOfT);
  }

  public static <T> T convertJSONObjectToObject(JSONObject jsonObject, Class<T> classOfT) {
    return getInstance().fromJson(jsonObject.toString(), classOfT);
  }

  public static String ObjectToJSONString(Object o) {
    return getGson().toJson(o);
  }

  public static JSONObject ObjectToJSONObject(Object o) {
    return new JSONObject(getGson().toJson(o));
  }

  public static <T> T JsonStringToObject(String jsonString, Class<T> classOfT) {
    return getGson().fromJson(jsonString, classOfT);
  }

  public static <T> T JsonObjectToObject(JSONObject jsonObject, Class<T> classOfT) {
    return getGson().fromJson(jsonObject.toString(), classOfT);
  }

  // Exclusion strategies for @Expose annotation
  private static class DeserializationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
      Expose expose = f.getAnnotation(Expose.class);
      return expose != null && !expose.deserialize();
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
      return false;
    }
  }

  // Exclusion strategies for @Expose annotation
  private static class SerializationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
      Expose expose = f.getAnnotation(Expose.class);
      return expose != null && !expose.serialize();
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
      return false;
    }
  }

  // Exclusion strategy for CSV fields
  private static class CsvExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
      return fieldAttributes.getAnnotation(CsvField.class) == null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
      return false;
    }
  }

  // Field naming strategy for CSV fields
  public static class CsvFieldNamingStrategy extends PropertyNamingStrategy
      implements FieldNamingStrategy {
    @Override
    public String translateName(Field field) {
      return CsvStrategies.fieldNamingStrategy(field);
    }
  }

  // Type adapter factory for CSV List handling
  private static class CsvTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> com.google.gson.TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      Class<T> rawType = (Class<T>) typeToken.getRawType();

      if (!List.class.isAssignableFrom(rawType)) {
        return null;
      }

      // Get the generic type parameter of the List
      Type elementType = null;
      if (typeToken.getType() instanceof ParameterizedType parameterizedType) {
        elementType = parameterizedType.getActualTypeArguments()[0];
      }

      if (elementType == null) {
        return null;
      }

      // Get the delegate adapter for normal JSON array handling
      final com.google.gson.TypeAdapter<T> delegateAdapter =
          gson.getDelegateAdapter(this, typeToken);

      return (com.google.gson.TypeAdapter<T>) new CsvListAdapter(elementType, delegateAdapter);
    }

    private static class CsvListAdapter<T> extends com.google.gson.TypeAdapter<List<?>> {
      private final Type elementType;
      private final com.google.gson.TypeAdapter<T> delegateAdapter;

      CsvListAdapter(Type elementType, com.google.gson.TypeAdapter<T> delegateAdapter) {
        this.elementType = elementType;
        this.delegateAdapter = delegateAdapter;
      }

      @Override
      public void write(JsonWriter jsonWriter, List<?> list) throws IOException {

        if (list == null) {
          jsonWriter.nullValue();
          return;
        }

        // Write as JSON string for CSV compatibility
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
          if (i > 0) sb.append(",");
          Object elem = list.get(i);
          if (elem instanceof Enum) {
            sb.append("\"").append(((Enum<?>) elem).name()).append("\"");
          } else if (elem instanceof String) {
            sb.append("\"").append(elem).append("\"");
          } else {
            sb.append(elem);
          }
        }
        sb.append("]");

        String result = sb.toString();
        jsonWriter.value(result);
      }

      @Override
      public List<?> read(JsonReader jsonReader) throws IOException {
        List<Object> result = new ArrayList<>();

        // Check if it's a string (CSV format) or array (JSON format)
        JsonToken token = jsonReader.peek();

        if (token == JsonToken.STRING) {
          // Handle CSV string format: "[\"GET\",\"POST\"]"
          String arrayString = jsonReader.nextString();

          // Remove brackets and split
          arrayString = arrayString.trim();

          if (arrayString.isEmpty()) {
            return result;
          }

          if (arrayString.startsWith("[") && arrayString.endsWith("]")) {
            arrayString = arrayString.substring(1, arrayString.length() - 1);
          }

          if (!arrayString.isEmpty()) {
            // Split by comma (but handle quoted commas properly)
            List<String> elements = splitCsvElements(arrayString);

            for (String s : elements) {
              String element = s.trim();

              // Remove quotes
              if (element.startsWith("\"") && element.endsWith("\"")) {
                element = element.substring(1, element.length() - 1);
              }

              Object parsed = parseElement(element);
              // Only add to result if parsing was successful (not null)
              if (parsed != null) {
                result.add(parsed);
              }
            }
          }
        } else if (token == JsonToken.BEGIN_ARRAY) {
          // Use the delegate adapter for normal JSON array
          return (List<?>) delegateAdapter.read(jsonReader);
        } else if (token == JsonToken.NULL) {
          jsonReader.nextNull();
          return null;
        }
        return result;
      }

      private List<String> splitCsvElements(String input) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < input.length(); i++) {
          char c = input.charAt(i);

          if (c == '"') {
            inQuotes = !inQuotes;
            current.append(c);
          } else if (c == ',' && !inQuotes) {
            result.add(current.toString());
            current = new StringBuilder();
          } else {
            current.append(c);
          }
        }

        if (!current.isEmpty()) {
          result.add(current.toString());
        }

        return result;
      }

      private Object parseElement(String value) {

        if (elementType instanceof Class<?> elementClass) {
          if (elementClass.isEnum()) {
            try {
              // Try case-insensitive enum matching
              @SuppressWarnings({"unchecked", "rawtypes"})
              Class<Enum> enumClass = (Class<Enum>) elementClass;

              for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(value.trim())) {
                  return enumConstant;
                }
              }
              // If no match found, return null
              return null;
            } catch (Exception ignored) {
              return null;
            }
          } else if (elementClass == Integer.class || elementClass == int.class) {
            try {
              return Integer.parseInt(value);
            } catch (NumberFormatException e) {
              return null; // Skip invalid integers
            }
          } else if (elementClass == String.class) {
            return value;
          }
        }
        return value;
      }
    }
  }

  // Date type adapter
  public static class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private final ThreadLocal<SimpleDateFormat> dateFormat =
        ThreadLocal.withInitial(
            () -> new SimpleDateFormat(DateTimeProps.YYYY_MM_DD_T_HH_MM_SS_SSS_XXX));

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
      if (date == null) {
        return JsonNull.INSTANCE;
      }

      try {
        ZoneId currentZone = DateParser.getSystemZoneId();
        SimpleDateFormat sdf = dateFormat.get();
        sdf.setTimeZone(TimeZone.getTimeZone(currentZone));
        String dateString = sdf.format(date);
        return new JsonPrimitive(dateString);
      } catch (Exception e) {
        ApplicationLogger.error("Error serializing date: {}", date);
        // Fallback to timestamp
        return new JsonPrimitive(date.getTime());
      }
    }

    @Override
    public Date deserialize(
        JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

      if (jsonElement == null || jsonElement.isJsonNull()) {
        return null;
      }

      if (jsonElement.isJsonPrimitive()) {
        JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();

        if (primitive.isNumber()) {
          // Handle numeric timestamps
          long timestamp = primitive.getAsLong();
          return DateParser.parseTimestamp(timestamp);
        } else if (primitive.isString()) {
          // Handle string dates
          String dateString = primitive.getAsString();
          Date result = DateParser.deserializeDate(dateString);
          if (result == null) {
            ApplicationLogger.error("Unable to parse date: {}", dateString);
            return null;
          }
          return result;
        }
      }

      throw new JsonParseException("Invalid date format: " + jsonElement);
    }
  }

  // Time type adapter
  public static class TimeTypeAdapter implements JsonSerializer<Time>, JsonDeserializer<Time> {

    private final ThreadLocal<DateFormat> timeFormat =
        ThreadLocal.withInitial(() -> new SimpleDateFormat(DateTimeProps.HH_MM_SS_SSS));

    @Override
    public JsonElement serialize(Time time, Type typeOfSrc, JsonSerializationContext context) {
      if (time == null) {
        return JsonNull.INSTANCE;
      }

      try {
        String timeString = timeFormat.get().format(time);
        return new JsonPrimitive(timeString);
      } catch (Exception e) {
        ApplicationLogger.error("Unable to parse time: {}", time);
        // Fallback to timestamp
        return new JsonPrimitive(time.getTime());
      }
    }

    @Override
    public Time deserialize(
        JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

      if (jsonElement == null || jsonElement.isJsonNull()) {
        return null;
      }

      if (jsonElement.isJsonPrimitive()) {
        JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();

        if (primitive.isNumber()) {
          // Handle numeric timestamps for time
          long timestamp = primitive.getAsLong();
          Date date = DateParser.parseTimestamp(timestamp);
          return new Time(date.getTime());
        } else if (primitive.isString()) {
          // Handle string time
          String timeString = primitive.getAsString();
          Time result = DateParser.deserializeTime(timeString);
          if (result == null) {
            ApplicationLogger.error("Unable to parse time: {}", timeString);
            return null;
          }
          return result;
        }
      }

      throw new JsonParseException("Invalid time format: " + jsonElement);
    }
  }

  public static class LocalDateTypeAdapter
      implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public JsonElement serialize(
        LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
      if (localDate == null) {
        return JsonNull.INSTANCE;
      }
      return new JsonPrimitive(localDate.format(FORMATTER));
    }

    @Override
    public LocalDate deserialize(
        JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
      if (jsonElement == null
          || jsonElement.isJsonNull()
          || jsonElement.getAsString().trim().isEmpty()) {
        return null;
      }
      return LocalDate.parse(jsonElement.getAsString(), FORMATTER);
    }
  }

  public static class LocalDateTimeTypeAdapter
      implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(
        LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
      if (localDateTime == null) {
        return JsonNull.INSTANCE;
      }
      return new JsonPrimitive(localDateTime.format(FORMATTER));
    }

    @Override
    public LocalDateTime deserialize(
        JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
      if (jsonElement == null
          || jsonElement.isJsonNull()
          || jsonElement.getAsString().trim().isEmpty()) {
        return null;
      }
      return LocalDateTime.parse(jsonElement.getAsString(), FORMATTER);
    }
  }

  // String type adapter that trims strings and converts empty strings to null
  private static class StringTypeAdapter
      implements JsonSerializer<String>, JsonDeserializer<String> {
    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src);
    }

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      String value = json.getAsString();
      if (value != null && !value.isEmpty()) {
        if (typeOfT.equals(String.class)) {
          return value.trim();
        }
      }
      return null;
    }
  }

  // Lenient number serializer/deserializer
  private static class NumberTypeAdapter
      implements JsonSerializer<Number>, JsonDeserializer<Number> {

    @Override
    public JsonElement serialize(
        Number number, Type type, JsonSerializationContext jsonSerializationContext) {
      if (number == null) {
        return null; // JSON null
      }
      return new JsonPrimitive(number);
    }

    @Override
    public Number deserialize(
        JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
      if (jsonElement == null || jsonElement.isJsonNull()) return null;

      String str = jsonElement.getAsString().trim();
      if (str.isEmpty()) return null;

      try {
        if (type == Integer.class || type == int.class) {
          return Integer.parseInt(str);
        } else if (type == Long.class || type == long.class) {
          return Long.parseLong(str);
        } else if (type == Double.class || type == double.class) {
          return Double.parseDouble(str);
        } else if (type == Float.class || type == float.class) {
          return Float.parseFloat(str);
        } else if (type == Short.class || type == short.class) {
          return Short.parseShort(str);
        } else if (type == Byte.class || type == byte.class) {
          return Byte.parseByte(str);
        }
      } catch (NumberFormatException e) {
        ApplicationLogger.error(e.getMessage(), e);
      }
      return null;
    }
  }

  // Enum type adapter factory for case-insensitive enum deserialization
  private static class EnumTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> com.google.gson.TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      Class<T> rawType = (Class<T>) typeToken.getRawType();
      if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
        return null;
      }
      // Handle potential anonymous subclasses
      if (!rawType.isEnum()) {
        rawType = (Class<T>) rawType.getSuperclass();
      }
      return (com.google.gson.TypeAdapter<T>) new CaseInsensitiveEnumAdapter(rawType);
    }
  }

  // Class type adapter to serialize/deserialize Class<?> objects
  public static class ClassTypeAdapter
      implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {
    @Override
    public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src.getSimpleName()); // serialize as fully-qualified class name
    }

    @Override
    public Class<?> deserialize(
        JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
      String className = jsonElement.getAsString();
      try {
        return Class.forName(className);
      } catch (ClassNotFoundException e) {
        ApplicationLogger.error("Class not found during deserialization: {}", className);
        return null;
      }
    }
  }

  // Case-insensitive enum adapter
  private static class CaseInsensitiveEnumAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    private final Class<T> enumType;

    CaseInsensitiveEnumAdapter(Class<T> enumType) {
      this.enumType = enumType;
    }

    @Override
    public void write(JsonWriter jsonWriter, T t) throws IOException {
      if (t == null) {
        jsonWriter.nullValue();
      } else {
        jsonWriter.value(t.name()); // standard serialization
      }
    }

    @Override
    public T read(JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      if (value == null) return null;

      for (T constant : enumType.getEnumConstants()) {
        if (constant.name().equalsIgnoreCase(value.trim())) {
          return constant;
        }
      }
      return null;
    }
  }

  // Lenient type adapter factory that skips bad values during deserialization
  private static class LenientTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, typeToken);
      return new TypeAdapter<T>() {

        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
          delegate.write(jsonWriter, t);
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
          try {
            return delegate.read(jsonReader);
          } catch (IllegalStateException | JsonSyntaxException e) {
            jsonReader.skipValue(); // Skip the bad value
            return null;
          }
        }
      };
    }
  }
}
