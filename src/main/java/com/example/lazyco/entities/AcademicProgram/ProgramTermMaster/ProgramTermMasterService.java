package com.example.lazyco.entities.AcademicProgram.ProgramTermMaster;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProgramTermMasterService
    extends CommonAbstractService<ProgramTermMasterDTO, ProgramTermMaster> {
  protected ProgramTermMasterService(ProgramTermMasterMapper programTermMasterMapper) {
    super(programTermMasterMapper);
  }

  @Override
  protected void validateBeforeCreate(ProgramTermMasterDTO request) {
    if (request.getProgramTermSystemId() == null) {
      throw new ApplicationException(ProgramTermMasterMessage.PROGRAM_TERM_SYSTEM_ID_IS_REQUIRED);
    }

    if (request.getTermSequence() == null || request.getTermSequence() < 0) {
      throw new ApplicationException(
          ProgramTermMasterMessage.PROGRAM_TERM_MASTER_INVALID_SEQUENCE_NUMBER);
    }

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(ProgramTermMasterMessage.PROGRAM_TERM_MASTER_NAME_IS_REQUIRED);
    }
    validateUniqueName(request, ProgramTermMasterMessage.DUPLICATE_PROGRAM_TERM_MASTER_NAME);
  }

  @Override
  protected void preCreate(ProgramTermMasterDTO request, ProgramTermMaster entityToCreate) {
    validateSequenceNumber(entityToCreate);
  }

  @Override
  protected void validateBeforeUpdate(ProgramTermMasterDTO request) {
    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, ProgramTermMasterMessage.DUPLICATE_PROGRAM_TERM_MASTER_NAME);
    }
  }

  @Override
  protected void preUpdate(
      ProgramTermMasterDTO request,
      ProgramTermMasterDTO entityBeforeUpdates,
      ProgramTermMaster entityToUpdate) {
    if (request.getTermSequence() != null
        && !request.getTermSequence().equals(entityToUpdate.getTermSequence())) {
      if (request.getTermSequence() < 0) {
        throw new ApplicationException(
            ProgramTermMasterMessage.PROGRAM_TERM_MASTER_INVALID_SEQUENCE_NUMBER);
      }
      validateSequenceNumber(entityToUpdate);
    }
  }

  private void validateSequenceNumber(ProgramTermMaster entity) {
    ProgramTermMasterDTO filter = new ProgramTermMasterDTO();
    filter.setProgramTermSystemId(entity.getProgramTermSystem().getId());
    filter.setTermSequence(entity.getTermSequence());
    if (entity.getId() != null) {
      filter.setIdsNotIn(List.of(entity.getId()));
    }
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          ProgramTermMasterMessage.PROGRAM_TERM_MASTER_SEQUENCE_NUMBER_ALREADY_IN_USE);
    }
  }
}
