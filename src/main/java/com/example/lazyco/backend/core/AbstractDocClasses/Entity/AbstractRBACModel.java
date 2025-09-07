package com.example.lazyco.backend.core.AbstractDocClasses.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRBACModel extends AbstractModel {
  private String userGroup;
}
