package com.example.lazyco.entities.SyllabusMaster;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class SyllabusMasterService
    extends CommonAbstractService<SyllabusMasterDTO, SyllabusMaster> {
  protected SyllabusMasterService(SyllabusMasterMapper syllabusMasterMapper) {
    super(syllabusMasterMapper);
  }
}
