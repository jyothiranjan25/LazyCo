package com.example.lazyco.core.AbstractClasses.DAO;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import java.util.List;
import java.util.function.BiConsumer;

public interface IAbstractDAO<D extends AbstractDTO<D>, E extends AbstractModel>
    extends IPersistenceDAO<E> {

  List<E> get(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);

  List<D> get(
      D filter,
      AbstractMapper<D, E> mapper,
      BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);

  Long getCount(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);
}
