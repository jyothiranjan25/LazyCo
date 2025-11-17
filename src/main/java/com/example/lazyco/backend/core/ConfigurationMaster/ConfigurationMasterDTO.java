package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ConfigurationMaster.class)
public class ConfigurationMasterDTO extends AbstractDTO<ConfigurationMasterDTO> {

  @InternalFilterableField private String configKey;

  @InternalFilterableField private String configValue;

  @InternalFilterableField private String description;

  @InternalFilterableField private Boolean sensitive;
}
