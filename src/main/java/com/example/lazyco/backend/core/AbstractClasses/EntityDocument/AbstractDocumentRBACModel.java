package com.example.lazyco.backend.core.AbstractClasses.EntityDocument;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractDocumentRBACModel extends AbstractDocumentModel {
  private String userGroup;
}
