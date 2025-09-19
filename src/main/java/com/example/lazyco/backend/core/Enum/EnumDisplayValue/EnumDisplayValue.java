package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Table(name = "enum_display_value")
@Audited
@EntityListeners(EnumDisplayValueListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EnumDisplayValue extends AbstractRBACModel {

  @Column(name = "enum_code")
  private String enumCode;

  @Column(name = "display_value")
  private String displayValue;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private EnumCategory category;
}
