package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
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
@Table(name = "applicant", comment = "Table storing applicant details")
@EntityListeners(ApplicantListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Applicant extends AbstractModel {

  @Column(name = "email", nullable = false, comment = "email address of the applicant")
  private String email;

  @Column(name = "phone_number", nullable = false, comment = "phone number of the applicant")
  private String phoneNumber;
}
