package com.example.lazyco.backend.core.AbstractDocumentClasses.ServiceDocument;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocumentClasses.EntityDocument.AbstractDocumentModel;
import com.example.lazyco.backend.core.AbstractClasses.ServiceComponents.ICRUDService;

import java.util.List;

public interface IAbstractService<D extends AbstractDTO<D>, E extends AbstractDocumentModel>
    extends ICRUDService<D> {

  List<E> getEntities(D filters);

  E getSingleEntity(D filter);

  E getEntityById(Long id);
}
