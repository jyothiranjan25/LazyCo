package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AcademicYear.class)
public class AcademicYearDTO extends AbstractDTO<AcademicYearDTO> {

  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  @InternalFilterableField private LocalDate startDate;
  @InternalFilterableField private LocalDate endDate;
  @InternalFilterableField private Boolean isActive;
}
