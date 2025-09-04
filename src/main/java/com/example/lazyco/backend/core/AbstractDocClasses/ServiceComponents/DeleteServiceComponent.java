package com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;

public interface DeleteServiceComponent<D extends AbstractDTO<D>> {
  D delete(D dto);
}
