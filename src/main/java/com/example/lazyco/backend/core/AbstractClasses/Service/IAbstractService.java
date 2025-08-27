package com.example.lazyco.backend.core.AbstractClasses.Service;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents.ICRUDService;
import java.util.List;

public interface IAbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    extends ICRUDService<D> {

  List<E> getEntities(D filters);

  E getSingleEntity(D filter);

  E getEntityById(Long id);
}
