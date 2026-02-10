package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ProgramCycleService extends CommonAbstractService<ProgramCycleDTO, ProgramCycle> {
  protected ProgramCycleService(ProgramCycleMapper programCycleMapper) {
    super(programCycleMapper);
  }
}
