package com.example.lazyco.entities.TermSystem;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.TermMaster.TermMasterMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {TermMasterMapper.class})
public interface TermSystemMapper extends AbstractMapper<TermSystemDTO, TermSystem> {

  @Named("ignoreTermMasters")
  @Mapping(target = "termMasters", ignore = true)
  TermSystemDTO ignoreTermMasters(TermSystem entity);

  @Override
  default List<TermSystemDTO> map(List<TermSystem> entities, TermSystemDTO filter) {
    if (filter != null && Boolean.FALSE.equals(filter.getFetchTermMasters())) {
      return entities.stream().map(this::ignoreTermMasters).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}
