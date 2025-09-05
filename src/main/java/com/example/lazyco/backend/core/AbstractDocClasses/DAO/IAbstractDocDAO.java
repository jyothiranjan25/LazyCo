package com.example.lazyco.backend.core.AbstractDocClasses.DAO;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;

public interface IAbstractDocDAO<D extends AbstractDTO<D>, E extends AbstractModel>
    extends IPersistenceDocDAO<E> {

  //  List<E> get(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);
  //
  //  List<D> get(
  //      D filter,
  //      AbstractMapper<D, E> mapper,
  //      BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);
  //
  //  Long getCount(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);
}
