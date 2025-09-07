package com.example.lazyco.backend.core.AbstractClasses.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractRBACModel extends AbstractModel {

  @Column(
      name = "user_group",
      nullable = false,
      length = 50,
      comment = "Role Based Access Control user group")
  private String userGroup;
}
