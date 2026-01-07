package com.example.lazyco.core.Exceptions;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SimpleResponseDTO extends AbstractDTO<SimpleResponseDTO> {
  private HttpStatus httpStatus;

  @Override
  public String toString() {
    return "SimpleResponseDTO [message=" + getMessage() + "]";
  }
}
