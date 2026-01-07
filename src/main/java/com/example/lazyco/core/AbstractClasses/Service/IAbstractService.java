package com.example.lazyco.core.AbstractClasses.Service;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.AbstractClasses.Service.ServiceComponents.ICRUDService;
import com.example.lazyco.core.AbstractClasses.Service.ServiceComponents.TransactionalService;
import java.util.List;

public interface IAbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    extends ICRUDService<D>, TransactionalService<D> {

  List<E> getEntities(D filters);

  E getSingleEntity(D filter);

  E getEntityById(Long id);
}
