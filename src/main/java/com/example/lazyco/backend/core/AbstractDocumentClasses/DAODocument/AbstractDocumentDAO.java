package com.example.lazyco.backend.core.AbstractDocumentClasses.DAODocument;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocumentClasses.EntityDocument.AbstractDocumentModel;
import com.example.lazyco.backend.core.AbstractDocumentClasses.MapperDocument.AbstractDocumentMapper;
import com.example.lazyco.backend.core.CriteriaBuilder.CriteriaBuilderWrapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.BiConsumer;

@Repository
public class AbstractDocumentDAO<D extends AbstractDTO<D>, E extends AbstractDocumentModel>
    extends PersistenceDocumentDAO<E> implements IAbstractDocumentDAO<D, E> {

  @Override
  public List<E> get(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters) {
    return List.of();
  }

  @Override
  public List<D> get(
      D filter,
      AbstractDocumentMapper<D, E> mapper,
      BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters) {
    return List.of();
  }

  @Override
  public Long getCount(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters) {
    return 0L;
  }
}
