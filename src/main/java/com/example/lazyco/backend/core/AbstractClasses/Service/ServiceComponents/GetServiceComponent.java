package com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;

public interface GetServiceComponent<D extends AbstractDTO<D>> {
    D get(D dto);
}
