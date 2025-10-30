package com.example.lazyco.backend.core.GosnConf;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

import com.example.lazyco.backend.core.CsvTemplate.CsvStrategies;
import com.example.lazyco.backend.core.DateUtils.DateParser;
import com.example.lazyco.backend.core.DateUtils.DateTimeProps;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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
    gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
    gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
    gsonBuilder.registerTypeAdapter(Time.class, new TimeDeserializer());
    gsonBuilder.registerTypeAdapter(Time.class, new TimeSerializer());
    gsonBuilder.registerTypeAdapter(String.class, new StringDeserializer());
    gsonBuilder.registerTypeAdapter(String.class, new StringSerializer());
    gsonBuilder.serializeSpecialFloatingPointValues();
    gsonBuilder.registerTypeAdapter(Number.class, new LenientNumberDeserializer());
    gsonBuilder.registerTypeAdapter(Number.class, new LenientNumberSerializer());
    gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
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

  public static class CsvFieldNamingStrategy extends PropertyNamingStrategy
      implements FieldNamingStrategy {
    @Override
    public String translateName(Field field) {
      return CsvStrategies.fieldNamingStrategy(field);
    }
  }

  private static class CsvTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      // Match List, ArrayList, etc.
      if (!List.class.isAssignableFrom(typeToken.getRawType())) {
        return null;
      }

      final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, typeToken);
      return new TypeAdapter<T>() {
        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
          delegate.write(jsonWriter, t);
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
          try {
            JsonElement jsonElement = JsonParser.parseReader(jsonReader);
            return (T) deserializeList(gson, jsonElement, typeToken.getType());
          } catch (IllegalStateException | JsonSyntaxException e) {
            jsonReader.skipValue(); // Skip the bad value
            return null;
          }
        }
      };
    }

    private List<?> deserializeList(Gson gson, JsonElement json, Type typeOfT) {
      if (json == null || json.isJsonNull()) {
        return Collections.emptyList();
      }

      try {
        // Normal array: ["GET","POST"]
        if (json.isJsonArray()) {
          return gson.fromJson(json, typeOfT);
        }

        // Quoted array string: "[\"GET\",\"POST\"]"
        if (json.isJsonPrimitive()) {
          String raw = json.getAsString().trim();

          if (raw.startsWith("[") && raw.endsWith("]")) {
            JsonArray inner = JsonParser.parseString(raw).getAsJsonArray();
            return gson.fromJson(inner, typeOfT);
          }

          // Single element: "GET" -> ["GET"]
          if (!raw.isEmpty()) {
            Type elementType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            Object single = gson.fromJson(new JsonPrimitive(raw), elementType);
            return Collections.singletonList(single);
          }
        }
      } catch (Exception e) {
        ApplicationLogger.error("Failed to parse list: {}", e.getMessage());
      }

      return Collections.emptyList();
    }
  }

  public static class DateDeserializer implements JsonDeserializer<Date> {
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

  public static class DateSerializer implements JsonSerializer<Date> {

    private final ThreadLocal<SimpleDateFormat> dateFormat =
        ThreadLocal.withInitial(
            () -> new SimpleDateFormat(DateTimeProps.YYYY_MM_DD_T_HH_MM_SS_SSS_XXX));

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
      if (date == null) {
        return JsonNull.INSTANCE;
      }

      try {
        ZoneId currentZone = DateParser.getSystemTimezone();
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
  }

  public static class TimeDeserializer implements JsonDeserializer<Time> {

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

  /** Thread-safe JSON serializer for Time objects. */
  public static class TimeSerializer implements JsonSerializer<Time> {

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
  }

  private static class StringDeserializer implements JsonDeserializer<String> {
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

  private static class StringSerializer implements JsonSerializer<String> {
    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src);
    }
  }

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

  private static class LenientNumberDeserializer implements JsonDeserializer<Number> {

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

  public static class LenientNumberSerializer implements JsonSerializer<Number> {
    @Override
    public JsonElement serialize(
        Number number, Type type, JsonSerializationContext jsonSerializationContext) {
      if (number == null) {
        return null; // JSON null
      }
      return new JsonPrimitive(number);
    }
  }

  private static class EnumTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      Class<T> rawType = (Class<T>) typeToken.getRawType();
      if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
        return null;
      }
      // Handle potential anonymous subclasses
      if (!rawType.isEnum()) {
        rawType = (Class<T>) rawType.getSuperclass();
      }
      return (TypeAdapter<T>) new CaseInsensitiveEnumAdapter(rawType);
    }
  }

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
}
