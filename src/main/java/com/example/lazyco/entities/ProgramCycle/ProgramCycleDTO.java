package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ProgramCycle.class)
public class ProgramCycleDTO extends AbstractDTO<ProgramCycleDTO> implements HasCode {

  private String code;
}
