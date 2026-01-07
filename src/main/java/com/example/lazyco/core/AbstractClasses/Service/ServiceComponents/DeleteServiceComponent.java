package com.example.lazyco.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;

public interface DeleteServiceComponent<D extends AbstractDTO<D>> {
  D delete(D dto);
}
