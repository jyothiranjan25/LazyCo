package com.example.lazyco.entities.AdmissionOffer;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicYear.AcademicYear;
import com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram.AdmissionOfferProgram;
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
    name = "admission_offer",
    comment = "Table storing admission offers for academic year.",
    indexes = {
      @Index(name = "idx_admission_offer_code", columnList = "code"),
      @Index(name = "idx_admission_offer_academic_year_id", columnList = "academic_year_id"),
      @Index(name = "idx_admission_offer_start_date", columnList = "start_date"),
      @Index(name = "idx_admission_offer_end_date", columnList = "end_date")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_admission_offer_code", columnNames = "code")})
@EntityListeners(AdmissionOfferListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdmissionOffer extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the admission offer")
  private String code;

  @Column(name = "name", comment = "Name of the admission offer")
  private String name;

  @Column(name = "description", comment = "Description of the admission offer")
  private String description;

  @Column(name = "start_date", comment = "Start date of the admission offer")
  private LocalDate startDate;

  @Column(name = "end_date", comment = "End date of the admission offer")
  private LocalDate endDate;

  @ManyToOne
  @JoinColumn(
      name = "academic_year_id",
      foreignKey = @ForeignKey(name = "fk_admission_offer_academic_year"),
      comment = "Reference to the academic year this admission offer belongs to.")
  private AcademicYear academicYear;

  @OneToMany(mappedBy = "admissionOffer", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AdmissionOfferProgram> offerPrograms;
}
