package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.entities.AcademicProgram.ProgramTermMaster.ProgramTermMasterDTO;
import com.example.lazyco.entities.AcademicProgram.ProgramTermMaster.ProgramTermMasterService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProgramCycleService extends CommonAbstractService<ProgramCycleDTO, ProgramCycle> {
  private final ProgramTermMasterService programTermMasterService;

  protected ProgramCycleService(
      ProgramCycleMapper programCycleMapper, ProgramTermMasterService programTermMasterService) {
    super(programCycleMapper);
    this.programTermMasterService = programTermMasterService;
  }

  @Override
  protected void validateBeforeCreate(ProgramCycleDTO request) {
    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_CODE_REQUIRED);
    }
    validateUniqueCode(request);

    if (request.getStartDate() == null || request.getEndDate() == null) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_DATES_REQUIRED);
    }

    if (request.getEndDate().isBefore(request.getStartDate())) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_INVALID_DATES);
    }

    if (request.getMinCredit() == null || request.getMaxCredit() == null) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_CREDITS_REQUIRED);
    }

    if (request.getMinCredit() < 0 || request.getMaxCredit() < 0) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_INVALID_CREDITS);
    }

    if (request.getMinCredit() > request.getMaxCredit()) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_CREDIT_VALIDATION);
    }

    if (request.getRegistrationStartDate() == null || request.getRegistrationEndDate() == null) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_REGISTRATION_DATE_REQUIRED);
    }

    if (request.getRegistrationEndDate().isBefore(request.getRegistrationStartDate())) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_INVALID_REGISTRATION_DATE);
    }

    if (request.getWithdrawalDeadline() == null) {
      throw new ApplicationException(
          ProgramCycleMessage.PROGRAM_CYCLE_WITHDRAWN_DEADLINE_DATE_REQUIRED);
    }

    if (request.getRegistrationEndDate().isAfter(request.getWithdrawalDeadline())) {
      throw new ApplicationException(
          ProgramCycleMessage.PROGRAM_CYCLE_INVALID_WITHDRAWN_DEADLINE_DATE);
    }

    if (request.getGradeSubmissionDeadline() == null) {
      throw new ApplicationException(
          ProgramCycleMessage.PROGRAM_CYCLE_GRADES_SUBMISSION_DEADLINE_DATE_REQUIRED);
    }

    if (request.getEndDate().isAfter(request.getGradeSubmissionDeadline().toLocalDate())) {
      throw new ApplicationException(
          ProgramCycleMessage.PROGRAM_CYCLE_INVALID_GRADES_SUBMISSION_DEADLINE_DATE);
    }

    if (request.getTermCycleId() == null) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_TERM_CYCLE_REQUIRED);
    }

    if (request.getProgramCurriculumId() == null) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_PROGRAM_CURRICULUM_REQUIRED);
    }

    if (request.getProgramTermMasterId() == null) {
      throw new ApplicationException(
          ProgramCycleMessage.PROGRAM_CYCLE_PROGRAM_TERM_MASTER_REQUIRED);
    }
    validateProgramCurriculumTermMaster(
        request.getProgramCurriculumId(), request.getProgramTermMasterId());
  }

  @Override
  protected void afterMakeUpdates(
      ProgramCycleDTO request, ProgramCycle beforeUpdates, ProgramCycle afterUpdates) {

    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (request.getStartDate() != null || request.getEndDate() != null) {
      if (afterUpdates.getEndDate().isAfter(afterUpdates.getStartDate())) {
        throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_INVALID_DATES);
      }
    }

    if (request.getMinCredit() != null || request.getMaxCredit() != null) {
      if (afterUpdates.getMinCredit() < 0 || afterUpdates.getMaxCredit() < 0) {
        throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_INVALID_CREDITS);
      }

      if (afterUpdates.getMinCredit() > afterUpdates.getMaxCredit()) {
        throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_CREDIT_VALIDATION);
      }
    }

    if (request.getRegistrationStartDate() != null || request.getRegistrationEndDate() != null) {
      if (afterUpdates.getRegistrationEndDate().isBefore(afterUpdates.getRegistrationStartDate())) {
        throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_INVALID_REGISTRATION_DATE);
      }
    }

    if (request.getWithdrawalDeadline() != null) {
      if (afterUpdates.getRegistrationEndDate().isBefore(afterUpdates.getWithdrawalDeadline())) {
        throw new ApplicationException(
            ProgramCycleMessage.PROGRAM_CYCLE_INVALID_WITHDRAWN_DEADLINE_DATE);
      }
    }

    if (request.getGradeSubmissionDeadline() != null) {
      if (afterUpdates
          .getEndDate()
          .isAfter(afterUpdates.getGradeSubmissionDeadline().toLocalDate())) {
        throw new ApplicationException(
            ProgramCycleMessage.PROGRAM_CYCLE_INVALID_GRADES_SUBMISSION_DEADLINE_DATE);
      }
    }

    if (request.getProgramCurriculumId() != null || request.getProgramTermMasterId() != null) {
      validateProgramCurriculumTermMaster(
          afterUpdates.getProgramCurriculum().getId(), afterUpdates.getProgramTermMaster().getId());
    }
  }

  private void validateProgramCurriculumTermMaster(
      Long programCurriculumId, Long programTermMasterId) {
    ProgramTermMasterDTO filter = new ProgramTermMasterDTO();
    filter.setProgramCurriculumId(programCurriculumId);
    List<ProgramTermMasterDTO> programTermMasters = programTermMasterService.get(filter);
    if (programTermMasters.stream().noneMatch(ptm -> ptm.getId().equals(programTermMasterId))) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_PROGRAM_TERM_MASTER_INVALID);
    }
  }
}
