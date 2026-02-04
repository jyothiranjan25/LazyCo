package com.example.lazyco.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.util.List;

public interface SearchServiceComponent<D extends AbstractDTO<D>> {
  List<D> search(D dto);
}
