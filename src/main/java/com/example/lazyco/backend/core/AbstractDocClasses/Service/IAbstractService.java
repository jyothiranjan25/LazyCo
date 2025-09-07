package com.example.lazyco.backend.core.AbstractDocClasses.Service;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents.ICRUDService;
import com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents.TransactionalService;
import java.util.List;

public interface IAbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    extends ICRUDService<D>, TransactionalService<D> {

  List<E> getEntities(D filters);

  E getSingleEntity(D filter);

  E getEntityById(String id);
}
