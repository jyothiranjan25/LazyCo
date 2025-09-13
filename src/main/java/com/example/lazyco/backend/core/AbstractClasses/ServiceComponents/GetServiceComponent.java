package com.example.lazyco.backend.core.AbstractClasses.ServiceComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import java.util.List;

public interface GetServiceComponent<D extends AbstractDTO<D>> {
  List<D> get(D dto);
}
