package com.example.lazyco.backend.core.AbstractClasses.Mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JacksonDepthHandler {
  // Max depth control never set to 0 to avoid infinite recursion
  private static final int MAX_DEPTH = 2;

  // ThreadLocal to track depth and avoid shared state issues in multi-threaded environments
  private static final ThreadLocal<Integer> CURRENT_DEPTH = ThreadLocal.withInitial(() -> 0);
  private static final ThreadLocal<Set<Integer>> SEEN_PATH = ThreadLocal.withInitial(HashSet::new);

  // Register this with your ObjectMapper
  public static void registerModule(ObjectMapper objectMapper) {
    SimpleModule module = new SimpleModule();
    module.setSerializerModifier(new DynamicDepthSerializerModifier());
    objectMapper.registerModule(module);
  }

  public static class DynamicDepthSerializerModifier extends BeanSerializerModifier {
    @Override
    public JsonSerializer<?> modifySerializer(
        SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
      if (serializer instanceof BeanSerializerBase) {
        return new DynamicDepthSerializer((BeanSerializerBase) serializer);
      }
      return serializer;
    }
  }

  private static class DynamicDepthSerializer extends JsonSerializer<Object> {

    private final BeanSerializerBase defaultSerializer;

    public DynamicDepthSerializer(BeanSerializerBase src) {
      this.defaultSerializer = src;
    }

    @Override
    public void serialize(
        Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
      int depth = CURRENT_DEPTH.get();
      int identity = System.identityHashCode(o);

      // Circular reference check
      if (SEEN_PATH.get().contains(identity)) {
        jsonGenerator.writeNull();
        return;
      }

      // Prevent infinite recursion by depth check
      if (depth >= MAX_DEPTH) {
        // Skip writing anything at max depth or circular reference
        jsonGenerator.writeNull();
        return;
      }

      // Increase depth
      CURRENT_DEPTH.set(depth + 1);
      SEEN_PATH.get().add(identity);

      try {
        // Serialize normally
        defaultSerializer.serialize(o, jsonGenerator, serializerProvider);
      } finally {
        // Decrease depth
        CURRENT_DEPTH.set(depth);
        SEEN_PATH.get().remove(identity);

        // Clean up ThreadLocal at the root level
        if (depth == 0) {
          CURRENT_DEPTH.remove();
          SEEN_PATH.remove();
        }
      }
    }

    // Optionally get "id" field via reflection
    private String tryGetId(Object obj) {
      try {
        var field = obj.getClass().getDeclaredField("id");
        field.setAccessible(true);
        Object idVal = field.get(obj);
        return idVal != null ? idVal.toString() : "unknown";
      } catch (Exception e) {
        return "unknown";
      }
    }
  }
}
