package com.example.lazyco.backend.core.AbstractClasses.ServiceComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;

public interface DeleteServiceComponent<D extends AbstractDTO<D>> {
  D delete(D dto);
}
