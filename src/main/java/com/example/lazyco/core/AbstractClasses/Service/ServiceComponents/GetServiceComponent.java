package com.example.lazyco.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.util.List;

public interface GetServiceComponent<D extends AbstractDTO<D>> {
  List<D> get(D dto);
}
