package com.example.lazyco.backend.core.AbstractClasses.Mapper;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.mapstruct.BeforeMapping;

public interface MapBidirectionalReference<D extends AbstractDTO<D>, E extends AbstractModel> {

  D map(E m);

  @BeforeMapping
  default D modifyEntityBeforeMapping(E source) {
    try {
      if (source == null || source.isSkipMapping()) {
        return null;
      }
      // Note: Don't use clone method as it won't clone the lazy loaded entities properly
      AbstractModelMapper modelMapper = new AbstractModelMapper();
      E mappedEntity = modelMapper.mapCircularReference(source);
      mappedEntity.setSkipMapping(true);
      return map(mappedEntity);
    } catch (Exception e) {
      ApplicationLogger.debug(e);
    }
    return null;
  }
}
