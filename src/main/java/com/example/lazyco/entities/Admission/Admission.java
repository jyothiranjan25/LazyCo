package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.ApplicationForm.ApplicationForm;
import com.example.lazyco.entities.ProgramCurriculum.ProgramCurriculum;
import com.example.lazyco.entities.ProgramCycle.ProgramCycle;
import com.example.lazyco.entities.Student.Student;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    name = "admission",
    comment = "table to store admission information",
    indexes = {
      @Index(name = "idx_admission_admission_number", columnList = "admissionNumber"),
      @Index(name = "idx_admission_university_number", columnList = "universityNumber"),
      @Index(name = "idx_admission_admission_status", columnList = "admissionStatus"),
      @Index(name = "idx_admission_application_form_id", columnList = "application_form_id"),
      @Index(name = "idx_admission_program_curriculum_id", columnList = "program_curriculum_id"),
      @Index(
          name = "idx_admission_joining_program_cycle_id",
          columnList = "joining_program_cycle_id"),
      @Index(
          name = "idx_admission_current_program_cycle_id",
          columnList = "current_program_cycle_id"),
      @Index(name = "idx_admission_student_id", columnList = "student_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_admission_admission_number", columnNames = "admissionNumber"),
      @UniqueConstraint(name = "uk_admission_university_number", columnNames = "universityNumber"),
      @UniqueConstraint(
          name = "uk_admission_application_form_id",
          columnNames = "application_form_id")
    })
@EntityListeners(AdmissionListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Admission extends AbstractRBACModel {

  @Column(name = "admission_number", nullable = false, comment = "Unique admission number")
  private String admissionNumber;

  @Column(
      name = "university_number",
      comment = "Unique university number assigned to the admitted student")
  private String universityNumber;

  @Column(name = "admission_date", nullable = false, comment = "date and time of admission")
  private LocalDateTime admissionDate;

  @Column(name = "official_email", comment = "official email assigned to the admitted student")
  private String officialEmail;

  @Column(name = "admission_status", nullable = false, comment = "current status of the admission")
  @Enumerated(EnumType.STRING)
  private AdmissionStatusEnum admissionStatus;

  @OneToOne
  @JoinColumn(
      name = "application_form_id",
      foreignKey = @ForeignKey(name = "fk_admission_application_form_id"),
      comment = "reference to the application form associated with this admission")
  private ApplicationForm applicationForm;

  @ManyToOne
  @JoinColumn(
      name = "program_curriculum_id",
      foreignKey = @ForeignKey(name = "fk_admission_program_curriculum_id"),
      nullable = false,
      comment = "reference to the program curriculum for which the student is admitted")
  private ProgramCurriculum programCurriculum;

  @ManyToOne
  @JoinColumn(
      name = "joining_program_cycle_id",
      foreignKey = @ForeignKey(name = "fk_admission_joining_program_cycle_id"),
      nullable = false,
      comment = "reference to the program cycle in which the student is joining")
  private ProgramCycle joiningProgramCycle;

  @ManyToOne
  @JoinColumn(
      name = "current_program_cycle_id",
      foreignKey = @ForeignKey(name = "fk_admission_current_program_cycle_id"),
      nullable = false,
      comment = "reference to the current program cycle of the student")
  private ProgramCycle currentProgramCycle;

  @ManyToOne
  @JoinColumn(
      name = "student_id",
      foreignKey = @ForeignKey(name = "fk_admission_student_id"),
      nullable = false,
      comment = "reference to the admitted student")
  private Student student;
}
