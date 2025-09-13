package com.example.lazyco.backend.core.AbstractDocClasses.DAO;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractDocClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.MongoCriteriaBuilder.MongoCriteriaBuilderWrapper;
import java.util.List;
import java.util.function.BiConsumer;

public interface IAbstractDocDAO<D extends AbstractDTO<D>, E extends AbstractModel>
    extends IPersistenceDocDAO<E> {

  List<E> get(D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters);

  List<D> get(
      D filter,
      AbstractMapper<D, E> mapper,
      BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters);

  Long getCount(D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters);
}
