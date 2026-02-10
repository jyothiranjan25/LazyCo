package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicProgram.AcademicProgram;
import com.example.lazyco.entities.AcademicProgram.ProgramTermSystem.ProgramTermSystem;
import com.example.lazyco.entities.AcademicYear.AcademicYear;
import com.example.lazyco.entities.TermSystem.TermSystem;
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
@Table(name = "sample")
@EntityListeners(ProgramCurriculumListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramCurriculum extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the program curriculum")
  private String code;

  @Column(name = "name", comment = "Name of the program curriculum")
  private String name;

  @Column(name = "description", comment = "Description of the program curriculum")
  private String description;

  @Column(name = "start_date", comment = "Start date of the program curriculum")
  private LocalDate startDate;

  @Column(name = "end_date", comment = "End date of the program curriculum")
  private LocalDate endDate;

  @Column(name = "conviction_date", comment = "Conviction date of the program curriculum")
  private LocalDate convictionDate;

  @Column(name = "admission_capacity", comment = "Admission capacity for the program curriculum")
  private Integer admissionCapacity;

  @ManyToOne
  @JoinColumn(
      name = "academic_year_id",
      foreignKey = @ForeignKey(name = "fk_program_curriculum_academic_year"),
      comment = "Foreign key referencing the academic year")
  private AcademicYear academicYear;

  @ManyToOne
  @JoinColumn(
      name = "term_system_id",
      foreignKey = @ForeignKey(name = "fk_program_curriculum_term_system"),
      comment = "Foreign key referencing the term system")
  private TermSystem termSystem;

  @ManyToOne
  @JoinColumn(
      name = "academic_program_id",
      foreignKey = @ForeignKey(name = "fk_program_curriculum_academic_program"),
      comment = "Foreign key referencing the academic program")
  private AcademicProgram academicProgram;

  @ManyToOne
  @JoinColumn(
      name = "program_term_system_id",
      foreignKey = @ForeignKey(name = "fk_program_curriculum_program_term_system"),
      comment = "Foreign key referencing the program term system")
  private ProgramTermSystem programTermSystem;
}
