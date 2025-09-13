package com.example.lazyco.backend.core.GosnConf;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

import com.example.lazyco.backend.core.DateUtils.DateParser;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    gsonBuilder.registerTypeAdapter(Number.class, new LenientNumberDeserializer());
    gsonBuilder.registerTypeAdapter(Number.class, new LenientNumberSerializer());
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

  private static class DateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(
        JsonElement jsonElement, Type typeOF, JsonDeserializationContext context)
        throws JsonParseException {

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
            throw new JsonParseException("Unable to parse date: " + dateString);
          }
          return result;
        }
      }

      throw new JsonParseException("Invalid date format: " + jsonElement);
    }
  }

  private static class DateSerializer implements JsonSerializer<Date> {
    private final ThreadLocal<DateFormat> dateFormat =
        ThreadLocal.withInitial(
            () -> {
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
              sdf.setTimeZone(TimeZone.getTimeZone(DateParser.getSystemTimezone()));
              return sdf;
            });

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
      String dateString = dateFormat.get().format(date);
      return new JsonPrimitive(dateString);
    }
  }

  private static class TimeSerializer implements JsonSerializer<Time> {
    private final ThreadLocal<DateFormat> dateFormat =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));

    @Override
    public JsonElement serialize(Time time, Type typeOfSrc, JsonSerializationContext context) {
      String dateString = dateFormat.get().format(time);
      return new JsonPrimitive(dateString);
    }
  }

  private static class TimeDeserializer implements JsonDeserializer<Time> {
    @Override
    public Time deserialize(
        JsonElement jsonElement, Type typeOF, JsonDeserializationContext context)
        throws JsonParseException {

      if (jsonElement.isJsonPrimitive()) {
        JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();

        if (primitive.isNumber()) {
          // Handle numeric timestamps for time
          long timestamp = primitive.getAsLong();
          // For Time, we typically want just the time portion
          Date date = DateParser.parseTimestamp(timestamp);
          return new Time(date.getTime());
        } else if (primitive.isString()) {
          // Handle string time
          String timeString = primitive.getAsString();
          Time result = DateParser.deserializeTime(timeString);
          if (result == null) {
            throw new JsonParseException("Unable to parse time: " + timeString);
          }
          return result;
        }
      }

      throw new JsonParseException("Invalid time format: " + jsonElement);
    }
  }

  private static class StringSerializer implements JsonSerializer<String> {
    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src);
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
}
