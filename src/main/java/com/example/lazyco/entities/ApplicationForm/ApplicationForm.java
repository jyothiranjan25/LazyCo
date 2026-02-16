package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.core.Utils.GenderEnum;
import com.example.lazyco.entities.AdmissionOffer.AdmissionOffer;
import com.example.lazyco.entities.ProgramCurriculum.ProgramCurriculum;
import com.example.lazyco.entities.ProgramCycle.ProgramCycle;
import jakarta.persistence.*;
import java.time.LocalDate;
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
    name = "application_form",
    comment = "Table storing application form details",
    indexes = {
      @Index(name = "idx_application_form_application_number", columnList = "applicationNumber"),
      @Index(name = "idx_application_form_email", columnList = "email"),
      @Index(name = "idx_application_form_phone_number", columnList = "phoneNumber"),
      @Index(name = "idx_application_form_admission_offer_id", columnList = "admission_offer_id"),
      @Index(
          name = "idx_application_form_program_curriculum_id",
          columnList = "program_curriculum_id"),
      @Index(
          name = "idx_application_form_starting_program_cycle_id",
          columnList = "starting_program_cycle_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_application_form_application_number",
          columnNames = "applicationNumber"),
      @UniqueConstraint(
          name = "uk_application_form_aoffer_id_email",
          columnNames = {"admission_offer_id", "email"}),
      @UniqueConstraint(
          name = "uk_application_form_aoffer_id_phone",
          columnNames = {"admission_offer_id", "phoneNumber"})
    })
@EntityListeners(ApplicationFormListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationForm extends AbstractRBACModel {

  @Column(name = "application_number", nullable = false, comment = "Unique application number")
  private String applicationNumber;

  @Column(name = "first_name", nullable = false, comment = "first name of the applicant")
  private String firstName;

  @Column(name = "middle_name", comment = "middle name of the applicant")
  private String middleName;

  @Column(name = "last_name", comment = "last name of the applicant")
  private String lastName;

  @Column(name = "gender", comment = "gender of the applicant")
  @Enumerated(EnumType.STRING)
  private GenderEnum gender;

  @Column(name = "date_of_birth", comment = "date of birth of the applicant")
  private LocalDate dateOfBirth;

  @Column(name = "email", nullable = false, comment = "email address of the applicant")
  private String email;

  @Column(name = "phone_number", nullable = false, comment = "phone number of the applicant")
  private String phoneNumber;

  @Column(name = "application_date", comment = "date when the application was submitted")
  private LocalDateTime applicationDate;

  @Column(
      name = "is_enrolled",
      columnDefinition = "boolean default false",
      comment = "indicates if the applicant is enrolled")
  private Boolean isEnrolled;

  @Column(name = "enrollment_date", comment = "date when the applicant enrolled")
  private LocalDate enrollmentDate;

  @Column(name = "raw_program_name", comment = "Raw program name as entered by the applicant")
  private String rawProgramName;

  @ManyToOne
  @JoinColumn(
      name = "admission_offer_id",
      foreignKey = @ForeignKey(name = "fk_application_form_admission_offer"),
      nullable = false,
      comment = "Foreign key referencing the admission offer")
  private AdmissionOffer admissionOffer;

  @ManyToOne
  @JoinColumn(
      name = "program_curriculum_id",
      foreignKey = @ForeignKey(name = "fk_application_form_program_curriculum"),
      comment = "Foreign key referencing the program curriculum")
  private ProgramCurriculum programCurriculum;

  @ManyToOne
  @JoinColumn(
      name = "starting_program_cycle_id",
      foreignKey = @ForeignKey(name = "fk_application_form_program_cycle"),
      comment = "Foreign key referencing the program cycle")
  private ProgramCycle startingProgramCycle;

  public String getFullName() {
    return mergeObject(firstName, middleName, lastName);
  }
}
