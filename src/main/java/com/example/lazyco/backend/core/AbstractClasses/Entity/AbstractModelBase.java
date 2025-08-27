package com.example.lazyco.backend.core.AbstractClasses.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractModelBase extends AbstractModel {

  @Column(name = "user_group")
  private String userGroup;
}
