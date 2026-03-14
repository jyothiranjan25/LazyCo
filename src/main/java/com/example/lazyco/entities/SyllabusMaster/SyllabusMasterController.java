package com.example.lazyco.entities.SyllabusMaster;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/syllabus_master")
public class SyllabusMasterController extends AbstractController<SyllabusMasterDTO> {
  public SyllabusMasterController(IAbstractService<SyllabusMasterDTO, ?> abstractService) {
    super(abstractService);
  }
}
