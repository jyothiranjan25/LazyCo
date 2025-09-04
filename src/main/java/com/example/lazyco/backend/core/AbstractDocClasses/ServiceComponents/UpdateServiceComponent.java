package com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;

public interface UpdateServiceComponent<D extends AbstractDTO<D>> {
  D update(D dto);
}
