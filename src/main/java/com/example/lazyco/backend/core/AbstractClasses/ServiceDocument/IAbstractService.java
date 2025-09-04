package com.example.lazyco.backend.core.AbstractClasses.ServiceDocument;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractClasses.EntityDocument.AbstractDocumentModel;
import com.example.lazyco.backend.core.AbstractClasses.ServiceComponents.ICRUDService;
import com.example.lazyco.backend.core.AbstractClasses.ServiceComponents.TransactionalService;

import java.util.List;

public interface IAbstractService<D extends AbstractDTO<D>, E extends AbstractDocumentModel>
    extends ICRUDService<D> {

  List<E> getEntities(D filters);

  E getSingleEntity(D filter);

  E getEntityById(Long id);
}
