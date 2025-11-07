package com.example.lazyco.backend.entities.UserManagement.Sample;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Sample.class)
public class SampleDTO extends AbstractDTO<SampleDTO> {}
