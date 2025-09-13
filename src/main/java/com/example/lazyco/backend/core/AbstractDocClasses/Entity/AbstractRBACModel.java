package com.example.lazyco.backend.core.AbstractDocClasses.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public abstract class AbstractRBACModel extends AbstractModel {
  @Field("user_group")
  private String userGroup;
}
