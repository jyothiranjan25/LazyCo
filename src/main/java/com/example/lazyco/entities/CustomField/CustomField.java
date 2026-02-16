package com.example.lazyco.entities.CustomField;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.Utils.FieldTypeEnum;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField.ApplicationFormSectionCustomField;
import com.example.lazyco.entities.CustomField.CustomFieldOption.CustomFieldOption;
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
    name = "custom_field",
    comment = "Table storing custom field details",
    indexes = {
      @Index(name = "idx_custom_field_name", columnList = "name"),
      @Index(name = "idx_custom_field_field_type", columnList = "field_type")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_custom_field_name", columnNames = "name")})
@EntityListeners(CustomFieldListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomField extends AbstractModel {

  @Column(name = "name", comment = "Name of the custom field")
  private String name;

  @Column(name = "key", comment = "Unique key for the custom field")
  private String key;

  @Column(name = "field_type", comment = "Type of the custom field input")
  @Enumerated(EnumType.STRING)
  private FieldTypeEnum fieldType;

  @OneToMany(mappedBy = "customField")
  private Set<ApplicationFormSectionCustomField> applicationFormSectionCustomFields;

  @OneToMany(mappedBy = "customField", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CustomFieldOption> customFieldOptions;
}
