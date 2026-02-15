package com.example.lazyco.entities.Document;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document")
public class DocumentController extends AbstractController<DocumentDTO> {
  public DocumentController(IAbstractService<DocumentDTO, ?> abstractService) {
    super(abstractService);
  }
}
