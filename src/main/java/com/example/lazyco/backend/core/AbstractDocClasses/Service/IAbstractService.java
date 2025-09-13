package com.example.lazyco.backend.core.AbstractDocClasses.Service;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents.ICRUDService;
import com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents.TransactionalService;
import com.example.lazyco.backend.core.MongoCriteriaBuilder.MongoCriteriaBuilderWrapper;
import java.util.List;
import java.util.function.BiConsumer;

public interface IAbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    extends ICRUDService<D>, TransactionalService<D> {

  List<E> getEntities(D filters);

  List<E> getEntities(D filters, BiConsumer<MongoCriteriaBuilderWrapper, D> additionalFilters);

  E getSingleEntity(D filter);

  E getEntityById(String id);

  List<D> get(D dto, BiConsumer<MongoCriteriaBuilderWrapper, D> additionalFilters);
}
