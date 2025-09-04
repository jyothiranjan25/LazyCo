package com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;

public interface CreateServiceComponent<D extends AbstractDTO<D>> {
  D create(D dto);
}
