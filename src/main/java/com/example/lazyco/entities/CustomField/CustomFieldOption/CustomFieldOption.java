package com.example.lazyco.entities.CustomField.CustomFieldOption;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.entities.CustomField.CustomField;
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
    name = "custom_field_option",
    comment = "Table storing custom field option details",
    indexes = {
      @Index(name = "idx_custom_field_option_value", columnList = "option_value"),
      @Index(name = "idx_custom_field_option_custom_field_id", columnList = "custom_field_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_custom_field_option_value_per_field",
          columnNames = {"option_value", "custom_field_id"})
    })
@EntityListeners(CustomFieldOptionListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomFieldOption extends AbstractModel {

  @Column(name = "option_value", comment = "Value of the custom field option")
  private String optionValue;

  @ManyToOne
  @JoinColumn(
      name = "custom_field_id",
      foreignKey = @ForeignKey(name = "fk_custom_field_option_custom_field"),
      nullable = false,
      comment = "Foreign key referencing the associated custom field")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private CustomField customField;
}
