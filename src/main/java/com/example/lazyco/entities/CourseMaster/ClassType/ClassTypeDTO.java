package com.example.lazyco.entities.CourseMaster.ClassType;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ClassType.class)
public class ClassTypeDTO extends AbstractDTO<ClassTypeDTO> implements HasName {

  private String name;
  private String description;
}
