package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AcademicYear.class)
public class AcademicYearDTO extends AbstractDTO<AcademicYearDTO> implements HasCodeAndName {

  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  @InternalFilterableField private LocalDate startDate;
  @InternalFilterableField private LocalDate endDate;
  @InternalFilterableField private Boolean isActive;

  /**
   * The following fields are used for date comparison filtering in the service layer. They are not
   * persisted in the database and are only used to pass parameters for filtering based on date
   * comparisons.
   */
  private LocalDate comparisonDate;

  private LocalDate startDateComparison;
  private LocalDate endDateComparison;
  private AcademicYearDTODateComparisonEnum dateComparison;
}
