package com.example.lazyco.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;

public interface ICRUDService<D extends AbstractDTO<D>>
    extends SearchServiceComponent<D>,
        GetServiceComponent<D>,
        CreateServiceComponent<D>,
        UpdateServiceComponent<D>,
        DeleteServiceComponent<D> {

  D getSingle(D filter);

  D getById(Long id);

  Long getCount(D filter);
}
