package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ProgramCurriculumMessage implements MessageCodes {
  ;

  private final String value;

  ProgramCurriculumMessage(String value) {
    this.value = value;
  }
}
