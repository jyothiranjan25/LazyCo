package com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AdmissionOffer.AdmissionOffer;
import com.example.lazyco.entities.ProgramCurriculum.ProgramCurriculum;
import com.example.lazyco.entities.ProgramCycle.ProgramCycle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
    name = "admission_offer_program",
    comment = "Table storing programs for admission offers.",
    indexes = {
      @Index(name = "idx_admission_offer_program_code", columnList = "code"),
      @Index(
          name = "idx_admission_offer_program_admission_offer_id",
          columnList = "admission_offer_id"),
      @Index(
          name = "idx_admission_offer_program_curriculum_id",
          columnList = "program_curriculum_id"),
      @Index(name = "idx_admission_offer_program_program_cycle_id", columnList = "program_cycle_id")
    })
@EntityListeners(AdmissionOfferProgramListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdmissionOfferProgram extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the raw admission offer program")
  private String code;

  @ManyToOne
  @JoinColumn(
      name = "admission_offer_id",
      foreignKey = @ForeignKey(name = "fk_admission_offer_program_admission_offer"),
      nullable = false,
      comment = "Reference to the admission offer this mapping belongs to.")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private AdmissionOffer admissionOffer;

  @ManyToOne
  @JoinColumn(
      name = "program_curriculum_id",
      foreignKey = @ForeignKey(name = "fk_admission_offer_program_curriculum"),
      nullable = false,
      comment = "Reference to the program curriculum for this admission offer program.")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ProgramCurriculum programCurriculum;

  @ManyToOne
  @JoinColumn(
      name = "program_cycle_id",
      foreignKey = @ForeignKey(name = "fk_admission_offer_program_program_cycle"),
      nullable = false,
      comment = "Reference to the program cycle for this admission offer program.")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ProgramCycle programCycle;
}
