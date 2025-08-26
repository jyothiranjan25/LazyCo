package com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;

public interface UpdateServiceComponent<D extends AbstractDTO<D>> {
    D update(D dto);
}
