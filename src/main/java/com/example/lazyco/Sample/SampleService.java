package com.example.lazyco.Sample;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class SampleService extends CommonAbstractService<SampleDTO, Sample> {
  protected SampleService(SampleMapper sampleMapper) {
    super(sampleMapper);
  }
}
