package com.example.lazyco.core.AbstractClasses.Service;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractHelperService<D extends AbstractDTO<D>, E extends AbstractModel> {

  public <T> void addAssociatedEntities(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (existingCollection == null) {
      existingCollection = new ArrayList<>();
    }

    if (incomingCollection == null) {
      return;
    }

    for (T entity : incomingCollection) {
      if (!existingCollection.contains(entity)) {
        existingCollection.add(entity);
      }
    }
  }

  public <T> void removeAssociatedEntities(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (existingCollection == null) {
      existingCollection = new ArrayList<>();
    }

    if (incomingCollection == null) {
      return;
    }
    for (T entity : incomingCollection) {
      existingCollection.remove(entity);
    }
  }
}
