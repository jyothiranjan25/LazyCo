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
  protected void validateBeforeCreate(ProgramTermMasterDTO requestDTO) {
    if (requestDTO.getProgramTermSystemId() == null) {
      throw new ApplicationException(ProgramTermMasterMessage.PROGRAM_TERM_SYSTEM_ID_IS_REQUIRED);
    }

    if (requestDTO.getTermSequence() == null || requestDTO.getTermSequence() < 0) {
      throw new ApplicationException(
          ProgramTermMasterMessage.PROGRAM_TERM_MASTER_INVALID_SEQUENCE_NUMBER);
    }

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(ProgramTermMasterMessage.PROGRAM_TERM_MASTER_NAME_IS_REQUIRED);
    }
    validateUniqueName(requestDTO, ProgramTermMasterMessage.DUPLICATE_PROGRAM_TERM_MASTER_NAME);
  }

  @Override
  protected void preCreate(ProgramTermMasterDTO requestDTO, ProgramTermMaster entityToCreate) {
    validateSequenceNumber(entityToCreate);
  }

  @Override
  protected void validateBeforeUpdate(ProgramTermMasterDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, ProgramTermMasterMessage.DUPLICATE_PROGRAM_TERM_MASTER_NAME);
    }
  }

  @Override
  protected void preUpdate(
      ProgramTermMasterDTO requestDTO,
      ProgramTermMasterDTO entityBeforeUpdates,
      ProgramTermMaster entityToUpdate) {
    if (requestDTO.getTermSequence() != null
        && !requestDTO.getTermSequence().equals(entityToUpdate.getTermSequence())) {
      if (requestDTO.getTermSequence() < 0) {
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
