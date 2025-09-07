package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleResponseDTO extends AbstractDTO<SimpleResponseDTO> {
  private String message;

  @Override
  public String toString() {
    return "SimpleResponseDTO [message=" + message + "]";
  }
}
