package com.example.lazyco.backend.entities.Sample;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SampleService extends AbstractService<SampleDTO, Sample> {
  protected SampleService(SampleMapper sampleMapper) {
    super(sampleMapper);
  }
}
