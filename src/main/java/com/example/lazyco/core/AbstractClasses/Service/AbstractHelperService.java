package com.example.lazyco.core.AbstractClasses.Service;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import java.util.Collection;
import java.util.HashSet;

public abstract class AbstractHelperService<D extends AbstractDTO<D>, E extends AbstractModel> {

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
