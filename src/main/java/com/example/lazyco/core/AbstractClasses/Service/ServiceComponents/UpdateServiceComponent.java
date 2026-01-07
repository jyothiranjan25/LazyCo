package com.example.lazyco.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;

public interface UpdateServiceComponent<D extends AbstractDTO<D>> {
  D update(D dto);
}
