package com.example.lazyco.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;

public interface CreateServiceComponent<D extends AbstractDTO<D>> {
  D create(D dto);
}
