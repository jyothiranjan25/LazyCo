package com.example.lazyco.core.CsvTemplate;

import com.example.lazyco.core.WebMVC.RequestHandling.QueryParams.QueryParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csv")
public class CsvTemplateController {

  private final CsvTemplateService csvTemplateService;

  public CsvTemplateController(CsvTemplateService csvTemplateService) {
    this.csvTemplateService = csvTemplateService;
  }

  @GetMapping
  public ResponseEntity<?> read(@QueryParams CsvTemplateDTO csvTemplateDTO) {
    return csvTemplateService.get(csvTemplateDTO);
  }
}
