package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class TermCycleService extends CommonAbstractService<TermCycleDTO, TermCycle> {
  protected TermCycleService(TermCycleMapper termCycleMapper) {
    super(termCycleMapper);
  }
}
