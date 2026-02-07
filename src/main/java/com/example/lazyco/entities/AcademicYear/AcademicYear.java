package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
    name = "academic_year",
    comment = "Table storing academic years",
    indexes = {
      @Index(name = "idx_academic_year_code", columnList = "code"),
      @Index(name = "idx_academic_year_name", columnList = "name")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_academic_year_code", columnNames = "code")})
@EntityListeners(AcademicYearListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AcademicYear extends AbstractModel {

  @Column(name = "code", comment = "Code representing the academic year")
  private String code;

  @Column(name = "name", comment = "Name of the academic year")
  private String name;

  @Column(name = "start_date", comment = "Start date of the academic year")
  private LocalDate startDate;

  @Column(name = "end_date", comment = "End date of the academic year")
  private LocalDate endDate;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default true",
      comment = "Whether this academic year is active")
  private Boolean isActive;
}
