package com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;

public interface CreateServiceComponent<D extends AbstractDTO<D>> {
    D create(D dto);
}
