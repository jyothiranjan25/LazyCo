package com.example.lazyco.backend.core.AbstractClasses.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractDocumentRBACModel extends AbstractDocumentModel {
  private String userGroup;
}
