package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.entities.ApplicationForm.ApplicationForm;
import jakarta.persistence.*;
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
    name = "applicant",
    comment = "Table storing applicant details",
    indexes = {
      @Index(name = "idx_applicant_email", columnList = "email"),
      @Index(name = "idx_applicant_phone_number", columnList = "phone_number")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_applicant_email", columnNames = "email"),
      @UniqueConstraint(name = "uk_applicant_phone_number", columnNames = "phone_number")
    })
@EntityListeners(ApplicantListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Applicant extends AbstractModel {

  @Column(name = "email", nullable = false, comment = "email address of the applicant")
  private String email;

  @Column(name = "phone_number", nullable = false, comment = "phone number of the applicant")
  private String phoneNumber;

  @OneToMany(mappedBy = "applicant")
  private Set<ApplicationForm> applicationForms;
}
