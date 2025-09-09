package com.example.lazyco.backend.core.AbstractDocClasses.DTO;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

@Getter
@Setter
public abstract class AbstractDTO<D> implements Serializable, Cloneable {

  protected String id;

  // List of objects for bulk operations
  private List<D> objectsList;

  private String ApiAction;

  // Pagination fields
  private Integer pageSize;
  private Integer pageOffset;

  @Expose(deserialize = false)
  private Long totalRecords;

  // Audit fields
  @Expose(serialize = false, deserialize = false)
  private String userGroup;

  @Expose(serialize = false, deserialize = false)
  private Date createdAt;

  @Expose(serialize = false, deserialize = false)
  private String createdBy;

  @Expose(serialize = false, deserialize = false)
  private Date updatedAt;

  @Expose(serialize = false, deserialize = false)
  private String updatedBy;

  @Expose(serialize = false, deserialize = false)
  private Boolean hasError;

  @Expose(deserialize = false)
  private String message;

  private Boolean isAtomicOperation;

  // filtering fields
  @Expose(serialize = false, deserialize = false)
  private List<String> idsIn;

  @Expose(serialize = false, deserialize = false)
  private List<String> idsNotIn;

  @Expose(serialize = false, deserialize = false)
  private Map<String, List<String>> stringIn;

  @Expose(serialize = false, deserialize = false)
  private Map<String, List<String>> stringNotIn;

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
