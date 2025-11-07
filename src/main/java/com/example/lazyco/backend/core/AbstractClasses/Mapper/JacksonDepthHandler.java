package com.example.lazyco.backend.core.AbstractClasses.Mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class JacksonDepthHandler {

  // Register this with your ObjectMapper
  public static void registerModule(ObjectMapper o, int maxDepth) {
    SimpleModule module = new SimpleModule();
    module.setSerializerModifier(new DynamicDepthSerializerModifier(maxDepth));
    o.registerModule(module);
  }

  public static class DynamicDepthSerializerModifier extends BeanSerializerModifier {
    private final int maxDepth;

    public DynamicDepthSerializerModifier(int maxDepth) {
      this.maxDepth = maxDepth;
    }

    @Override
    public JsonSerializer<?> modifySerializer(
        SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
      if (serializer instanceof BeanSerializerBase) {
        return new DynamicDepthSerializer((BeanSerializerBase) serializer, maxDepth);
      }
      return serializer;
    }
  }

  private static class DynamicDepthSerializer extends JsonSerializer<Object> {

    // ThreadLocal to track depth and avoid shared state issues in multi-threaded environments
    private static final ThreadLocal<Integer> CURRENT_DEPTH = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<Set<Integer>> SEEN_PATH =
        ThreadLocal.withInitial(HashSet::new);

    private final BeanSerializerBase defaultSerializer;
    // Max depth control never set to 0 to avoid infinite recursion
    private final int maxDepth;

    public DynamicDepthSerializer(BeanSerializerBase src, int maxDepth) {
      this.defaultSerializer = src;
      this.maxDepth = Math.max(1, maxDepth);
    }

    @Override
    public void serialize(Object o, JsonGenerator j, SerializerProvider s) throws IOException {
      int depth = CURRENT_DEPTH.get();
      int identity = System.identityHashCode(o);

      // Circular reference check
      //      if (SEEN_PATH.get().contains(identity)) {
      //        j.writeNull();
      //        return;
      //      }

      // Prevent infinite recursion by depth check
      if (depth >= maxDepth) {
        // Skip writing anything at max depth or circular reference
        j.writeNull();
        return;
      }

      // Increase depth
      CURRENT_DEPTH.set(depth + 1);
      SEEN_PATH.get().add(identity);

      try {
        // Serialize normally
        defaultSerializer.serialize(o, j, s);
      } finally {
        // Decrease depth
        SEEN_PATH.get().remove(identity);
        CURRENT_DEPTH.set(depth);

        // Clean up ThreadLocal at the root level
        if (depth == 0) {
          CURRENT_DEPTH.remove();
          SEEN_PATH.remove();
        }
      }
    }

    // Optionally get "id" field via reflection
    private Object tryGetId(Object obj) {
      if (obj == null) return null;

      Class<?> clazz = obj.getClass();

      while (clazz != null) {
        try {
          Field field = clazz.getDeclaredField("id");
          field.setAccessible(true);
          return field.get(obj);
        } catch (NoSuchFieldException ignored) {
          clazz = clazz.getSuperclass(); // keep searching
        } catch (Exception e) {
          return null;
        }
      }
      return null; // no "id" field found
    }
  }
}
