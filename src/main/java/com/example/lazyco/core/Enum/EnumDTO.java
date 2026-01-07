package com.example.lazyco.core.Enum;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnumDTO extends AbstractDTO<EnumDTO> {
  private String enumType;
  private Map<String, String> enumMap;
}
