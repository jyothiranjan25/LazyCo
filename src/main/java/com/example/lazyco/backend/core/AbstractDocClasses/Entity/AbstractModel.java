package com.example.lazyco.backend.core.AbstractDocClasses.Entity;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public abstract class AbstractModel implements Serializable, Cloneable {

  @Id
  private String id;

  @Field("created_at")
  private Date createdAt;

  @Field("updated_at")
  private Date updatedAt;

  @Field("created_by")
  private String createdBy;

  @Field("updated_by")
  private String updatedBy;

  @Transient
  @SuppressWarnings("rawtypes")
  private AbstractDTO dto;

  @Override
  public Object clone() {
    Object o1 = null;
    try {
      o1 = super.clone();
      // SerializationUtils.clone performs a deep clone
      return SerializationUtils.clone(this);
    } catch (Exception e) {
      return o1;
    }
  }
}
