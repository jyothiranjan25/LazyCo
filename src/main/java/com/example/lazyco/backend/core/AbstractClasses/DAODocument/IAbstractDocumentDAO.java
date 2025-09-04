package com.example.lazyco.backend.core.AbstractClasses.DocumentDAO;

import com.example.lazyco.backend.core.AbstractClasses.DAO.IPersistenceDAO;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractDocumentModel;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.CriteriaBuilder.CriteriaBuilderWrapper;

import java.util.List;
import java.util.function.BiConsumer;

public interface IAbstractDocumentDAO<D extends AbstractDTO<D>, E extends AbstractDocumentModel> {

  List<E> get(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);

  List<D> get(
      D filter,
      AbstractMapper<D, E> mapper,
      BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);

  Long getCount(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);
}
