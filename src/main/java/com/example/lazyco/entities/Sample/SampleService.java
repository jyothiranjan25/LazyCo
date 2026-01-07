package com.example.lazyco.entities.Sample;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class SampleService extends AbstractService<SampleDTO, Sample> {
  protected SampleService(SampleMapper sampleMapper) {
    super(sampleMapper);
  }
}
