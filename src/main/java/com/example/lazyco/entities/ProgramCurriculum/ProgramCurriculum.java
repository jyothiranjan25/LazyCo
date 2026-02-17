package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicProgram.AcademicProgram;
import com.example.lazyco.entities.AcademicProgram.ProgramTermSystem.ProgramTermSystem;
import com.example.lazyco.entities.AcademicYear.AcademicYear;
import com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram.AdmissionOfferProgram;
import com.example.lazyco.entities.ApplicationForm.ApplicationForm;
import com.example.lazyco.entities.ProgramCycle.ProgramCycle;
import com.example.lazyco.entities.TermSystem.TermSystem;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
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
    name = "program_curriculums",
    comment = "Table representing program curriculums",
    indexes = {
      @Index(name = "idx_program_curriculums_code", columnList = "code"),
      @Index(name = "idx_program_curriculums_name", columnList = "name"),
      @Index(name = "idx_program_curriculums_start_date", columnList = "start_date"),
      @Index(name = "idx_program_curriculums_end_date", columnList = "end_date"),
      @Index(name = "idx_program_curriculums_conviction_date", columnList = "conviction_date"),
      @Index(name = "idx_program_curriculums_academic_year_id", columnList = "academic_year_id"),
      @Index(name = "idx_program_curriculums_term_system_id", columnList = "term_system_id"),
      @Index(
          name = "idx_program_curriculums_academic_program_id",
          columnList = "academic_program_id"),
      @Index(
          name = "idx_program_curriculums_program_term_system_id",
          columnList = "program_term_system_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_program_curriculums_code", columnNames = "code")
    })
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

  @Column(
      name = "min_credit",
      comment = "min credit requirement for the program curriculum completion")
  private Integer minCredit;

  @Column(
      name = "max_credit",
      comment =
          "max credit can be taken for the program curriculum completion, if null then no max credit limit")
  private Integer maxCredit;

  @ManyToOne
  @JoinColumn(
      name = "academic_year_id",
      foreignKey = @ForeignKey(name = "fk_program_curriculum_academic_year"),
      nullable = false,
      comment = "Foreign key referencing the academic year")
  private AcademicYear academicYear;

  @ManyToOne
  @JoinColumn(
      name = "term_system_id",
      foreignKey = @ForeignKey(name = "fk_program_curriculum_term_system"),
      nullable = false,
      comment = "Foreign key referencing the term system")
  private TermSystem termSystem;

  @ManyToOne
  @JoinColumn(
      name = "academic_program_id",
      foreignKey = @ForeignKey(name = "fk_program_curriculum_academic_program"),
      nullable = false,
      comment = "Foreign key referencing the academic program")
  private AcademicProgram academicProgram;

  @ManyToOne
  @JoinColumn(
      name = "program_term_system_id",
      foreignKey = @ForeignKey(name = "fk_program_curriculum_program_term_system"),
      nullable = false,
      comment = "Foreign key referencing the program term system")
  private ProgramTermSystem programTermSystem;

  @OneToMany(mappedBy = "programCurriculum")
  private Set<ProgramCycle> programCycles;

  @OneToMany(mappedBy = "programCurriculum")
  private Set<AdmissionOfferProgram> admissionOfferPrograms;

  @OneToMany(mappedBy = "programCurriculum")
  private Set<ApplicationForm> applicationForms;
}
