package com.example.lazyco.entities.StudentFormStructure.StudentFormPage;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection.StudentFormPageSectionDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = StudentFormPage.class)
public class StudentFormPageDTO extends AbstractDTO<StudentFormPageDTO> implements HasName {

  @InternalFilterableField private String name;
  @InternalFilterableField private String key;
  private String description;
  private Integer order;
  private List<StudentFormPageSectionDTO> studentFormPageSections;
}
