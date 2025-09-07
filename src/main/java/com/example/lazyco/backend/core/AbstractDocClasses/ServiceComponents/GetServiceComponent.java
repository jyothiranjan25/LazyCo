package com.example.lazyco.backend.core.AbstractDocClasses.ServiceComponents;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import java.util.List;

public interface GetServiceComponent<D extends AbstractDTO<D>> {
  List<D> get(D dto);
}
