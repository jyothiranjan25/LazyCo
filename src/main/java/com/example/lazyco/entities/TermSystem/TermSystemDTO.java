package com.example.lazyco.entities.TermSystem;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMasterDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = TermSystem.class)
public class TermSystemDTO extends AbstractDTO<TermSystemDTO> implements HasCodeAndName {
  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  private List<TermMasterDTO> termMasters;
  private Boolean fetchTermMasters;
}
