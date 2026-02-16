package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ApplicationFormMapper extends AbstractMapper<ApplicationFormDTO, ApplicationForm> {

  @Mapping(target = "admissionOfferId", source = "admissionOffer.id")
    @Mapping(target = "programCurriculumId",source = "programCurriculum.id")
    @Mapping( target = "startingProgramCycleId",source = "startingProgramCycle.id")
  ApplicationFormDTO map(ApplicationForm entity);
}
