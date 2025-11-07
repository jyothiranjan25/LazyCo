package com.example.lazyco.backend.core.AbstractClasses.Mapper;

import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import com.example.lazyco.backend.core.JSONUtils.JSONUtils;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;

public class AbstractModelMapper {

  @Getter protected ModelMapper modelMapper;

  public AbstractModelMapper() {
    this(false);
  }

  public AbstractModelMapper(boolean skipNull) {
    this.modelMapper = new ModelMapper();
    this.modelMapper
        .getConfiguration()
        .setAmbiguityIgnored(true)
        .setMatchingStrategy(MatchingStrategies.STRICT)
        .setSkipNullEnabled(skipNull)
        .setPropertyCondition(
            (MappingContext<Object, Object> context) -> {
              if (!skipNull) return true;
              Object source = context.getSource();
              return source != null && (!(source instanceof String str) || !str.trim().isEmpty());
            });
  }

  public <D, T> D map(T source, Class<D> destinationClass) {
    return modelMapper.map(source, destinationClass);
  }

  public <D, T> D map(T source, D destination) {
    modelMapper.map(source, destination);
    return destination;
  }

  public <S, D> void map(
      Class<S> sourceClass,
      Class<D> destinationClass,
      java.util.function.Consumer<org.modelmapper.TypeMap<S, D>> configurer) {
    org.modelmapper.TypeMap<S, D> typeMap = modelMapper.getTypeMap(sourceClass, destinationClass);
    if (typeMap == null) {
      typeMap = modelMapper.createTypeMap(sourceClass, destinationClass);
    }
    configurer.accept(typeMap);
  }

  @SuppressWarnings("unchecked")
  public <E> E mapCircularReference(E source) {
    try {
      if (source == null) {
        return null;
      }
      // Note: Don't use clone method as it won't clone the lazy loaded entities properly
      // Create a new ObjectMapper instance for serialization
      ObjectMapper objectMapper = new ObjectMapper();
      JacksonDepthHandler.registerModule(objectMapper, 3); // Set max depth
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

      String jsonString = objectMapper.writeValueAsString(source);
      String cleanJsonString = JSONUtils.removeNumbersAndReferences(jsonString);

      E newEntity = (E) GsonSingleton.JsonStringToObject(cleanJsonString, source.getClass());

      E mappedEntity = (E) source.getClass().getDeclaredConstructor().newInstance();

      // Create a Map for the source and destination classes
      modelMapper.map(newEntity, mappedEntity);

      return mappedEntity;
    } catch (Exception e) {
      ApplicationLogger.error(e, e.getClass());
    }
    return source;
  }
}
