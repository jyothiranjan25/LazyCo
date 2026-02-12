package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicProgram.ProgramTermMaster.ProgramTermMaster;
import com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram.AdmissionOfferProgram;
import com.example.lazyco.entities.ProgramCurriculum.ProgramCurriculum;
import com.example.lazyco.entities.TermCycle.TermCycle;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    name = "program_cycle",
    comment = "Table storing program cycle details",
    indexes = {
      @Index(name = "idx_program_cycle_code", columnList = "code"),
      @Index(name = "idx_program_cycle_start_date", columnList = "start_date"),
      @Index(name = "idx_program_cycle_end_date", columnList = "end_date"),
      @Index(
          name = "idx_program_cycle_registration_start_date",
          columnList = "registration_start_date"),
      @Index(
          name = "idx_program_cycle_registration_end_date",
          columnList = "registration_end_date"),
      @Index(name = "idx_program_cycle_withdrawal_deadline", columnList = "withdrawal_deadline"),
      @Index(
          name = "idx_program_cycle_grade_submission_deadline",
          columnList = "grade_submission_deadline"),
      @Index(
          name = "idx_program_cycle_program_curriculum_id",
          columnList = "program_curriculum_id"),
      @Index(name = "idx_program_cycle_term_cycle_id", columnList = "term_cycle_id"),
      @Index(
          name = "idx_program_cycle_program_term_master_id",
          columnList = "program_term_master_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_program_cycle_code", columnNames = "code"),
      @UniqueConstraint(
          name = "uk_program_cycle_pcurriculum_tcycle_ptmaster_sdate",
          columnNames = {
            "program_curriculum_id",
            "term_cycle_id",
            "program_term_master_id",
            "start_date"
          })
    })
@EntityListeners(ProgramCycleListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramCycle extends AbstractRBACModel {

  @Column(name = "code", comment = "Code of the program cycle")
  private String code;

  @Column(name = "description", comment = "Description of the program cycle")
  private String description;

  @Column(name = "start_date", comment = "Start date of the program cycle")
  private LocalDate startDate;

  @Column(name = "end_date", comment = "End date of the program cycle")
  private LocalDate endDate;

  @Column(name = "min_credit", comment = "Minimum course credit requirement for the program cycle")
  private Integer minCredit;

  @Column(name = "max_credit", comment = "Maximum course credit requirement for the program cycle")
  private Integer maxCredit;

  @Column(
      name = "registration_start_date",
      comment = "Registration start date of the program cycle")
  private LocalDateTime registrationStartDate;

  @Column(name = "registration_end_date", comment = "Registration end date of the program cycle")
  private LocalDateTime registrationEndDate;

  @Column(
      name = "withdrawal_deadline",
      comment = "Deadline for students to withdraw from the program cycle")
  private LocalDateTime withdrawalDeadline;

  @Column(
      name = "disable_student_registration",
      comment = "Flag to disable student registration for the program cycle",
      columnDefinition = "boolean default false")
  private Boolean disableStudentRegistration;

  @Column(
      name = "min_registration_credit",
      comment = "Minimum course credit requirement for student registration in the program cycle")
  private Integer minRegistrationCredit;

  @Column(
      name = "max_registration_credit",
      comment = "Maximum course credit can be registered by a student in the program cycle")
  private Integer maxRegistrationCredit;

  @Column(
      name = "grade_submission_deadline",
      comment = "Deadline for instructors to submit grades for the program cycle")
  private LocalDateTime gradeSubmissionDeadline;

  @ManyToOne
  @JoinColumn(
      name = "program_curriculum_id",
      foreignKey = @ForeignKey(name = "fk_program_cycle_program_curriculum"),
      comment = "Foreign key referencing the program curriculum")
  private ProgramCurriculum programCurriculum;

  @ManyToOne
  @JoinColumn(
      name = "term_cycle_id",
      foreignKey = @ForeignKey(name = "fk_program_cycle_term_cycle"),
      comment = "Foreign key referencing the term cycle")
  private TermCycle termCycle;

  @ManyToOne
  @JoinColumn(
      name = "program_term_master_id",
      foreignKey = @ForeignKey(name = "fk_program_cycle_program_term_master"),
      comment = "Foreign key referencing the program term master")
  private ProgramTermMaster programTermMaster;

  @OneToMany(mappedBy = "programCycle")
  private Set<AdmissionOfferProgram> admissionOfferPrograms;
}
