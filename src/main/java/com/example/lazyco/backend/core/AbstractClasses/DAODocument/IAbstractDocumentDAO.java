package com.example.lazyco.backend.core.AbstractClasses.DAODocument;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.EntityDocument.AbstractDocumentModel;
import com.example.lazyco.backend.core.AbstractClasses.MapperDocument.AbstractDocumentMapper;
import com.example.lazyco.backend.core.CriteriaBuilder.CriteriaBuilderWrapper;
import java.util.List;
import java.util.function.BiConsumer;

public interface IAbstractDocumentDAO<D extends AbstractDTO<D>, E extends AbstractDocumentModel>
    extends IPersistenceDocumentDAO<E> {

  List<E> get(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);

  List<D> get(
      D filter,
      AbstractDocumentMapper<D, E> mapper,
      BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);

  Long getCount(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters);
}
