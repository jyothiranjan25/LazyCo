package com.example.lazyco.core.Enum;

import com.example.lazyco.core.Utils.ResponseUtils;
import com.example.lazyco.core.WebMVC.RequestHandling.QueryParams.QueryParams;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enum")
public class EnumController {

  private final EnumService enumService;

  public EnumController(EnumService enumService) {
    this.enumService = enumService;
  }

  @GetMapping
  public ResponseEntity<EnumDTO> read(@QueryParams EnumDTO enumDTO) {
    List<EnumDTO> enumDTOList = enumService.get(enumDTO);
    EnumDTO result = new EnumDTO();
    result.setObjects(enumDTOList);
    return ResponseUtils.sendResponse(result);
  }
}
