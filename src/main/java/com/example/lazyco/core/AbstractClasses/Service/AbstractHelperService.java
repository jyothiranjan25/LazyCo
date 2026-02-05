package com.example.lazyco.core.AbstractClasses.Service;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

public abstract class AbstractHelperService<D extends AbstractDTO<D>, E extends AbstractModel> {

  // Cache for DTO class calculation to avoid repeated reflection
  private static final ConcurrentHashMap<Class<?>, Class<?>> dtoClassCache =
      new ConcurrentHashMap<>();

  @Getter private final Class<D> dtoClass;

  protected AbstractHelperService() {
    dtoClass = this.calculateDTOClass();
  }

  @SuppressWarnings("unchecked")
  private Class<D> calculateDTOClass() {
    // Use cache to avoid repeated reflection
    return (Class<D>)
        dtoClassCache.computeIfAbsent(
            getClass(),
            clazz -> {
              Type superClass = clazz.getGenericSuperclass();
              if (superClass instanceof ParameterizedType parameterizedType) {
                Type type = parameterizedType.getActualTypeArguments()[0];
                if (type instanceof Class) {
                  return (Class<?>) type;
                } else if (type instanceof ParameterizedType) {
                  return (Class<?>) ((ParameterizedType) type).getRawType();
                }
              }
              throw new ExceptionWrapper("Unable to determine DTO class for: " + clazz.getName());
            });
  }

  public <T> void addAssociated(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (incomingCollection == null || existingCollection == null) {
      return;
    }

    for (T entity : incomingCollection) {
      if (!existingCollection.contains(entity)) {
        existingCollection.add(entity);
      }
    }
  }

  public <T> void removeAssociated(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (incomingCollection == null || existingCollection == null) {
      return;
    }

    for (T entity : incomingCollection) {
      existingCollection.remove(entity);
    }
  }

  public <T> Collection<T> addAssociatedEntities(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (existingCollection == null) {
      existingCollection = new HashSet<>();
    }

    if (incomingCollection != null) {
      for (T entity : incomingCollection) {
        if (!existingCollection.contains(entity)) {
          existingCollection.add(entity);
        }
      }
    }
    return existingCollection;
  }

  public <T> Collection<T> removeAssociatedEntities(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (existingCollection == null) {
      existingCollection = new HashSet<>();
    }

    if (incomingCollection != null) {
      for (T entity : incomingCollection) {
        existingCollection.remove(entity);
      }
    }
    return existingCollection;
  }
}
