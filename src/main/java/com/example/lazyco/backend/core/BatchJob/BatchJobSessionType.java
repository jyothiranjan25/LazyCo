package com.example.lazyco.backend.core.BatchJob;

import lombok.Getter;

@Getter
public enum BatchJobSessionType {
  SINGLE_OBJECT_COMMIT("Process items one by one"),
  ATOMIC_OPERATION("Process all items in single transaction");

  private final String description;

  BatchJobSessionType(String description) {
    this.description = description;
  }
}
