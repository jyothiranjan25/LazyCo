package com.example.lazyco.backend.core.AbstractClasses.DTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

@Getter
@Setter
public abstract class AbstractDTO<D> implements Serializable, Cloneable {

  protected Long id;
  private List<D> objectsList;
  private Integer pageSize;
  private Integer pageOffset;
  private Long totalRecords;
  @Expose
  private String userGroup;
  @Expose
  private Date createdAt;
    @Expose
  private String createdBy;
    @Expose
  private Date updatedAt;
    @Expose
  private String updatedBy;

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
