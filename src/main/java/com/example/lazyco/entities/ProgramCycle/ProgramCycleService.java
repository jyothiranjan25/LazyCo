package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProgramCycleService extends CommonAbstractService<ProgramCycleDTO, ProgramCycle> {
  protected ProgramCycleService(ProgramCycleMapper programCycleMapper) {
    super(programCycleMapper);
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

    if (request.getStartDate().isAfter(request.getEndDate())) {
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

    if (request.getRegistrationStartDate().isAfter(request.getRegistrationEndDate())) {
      throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_INVALID_REGISTRATION_DATE);
    }

    if (request.getWithdrawalDeadline() == null) {
      throw new ApplicationException(
          ProgramCycleMessage.PROGRAM_CYCLE_WITHDRAWN_DEADLINE_DATE_REQUIRED);
    }

    if (request.getWithdrawalDeadline().isAfter(request.getRegistrationEndDate())) {
      throw new ApplicationException(
          ProgramCycleMessage.PROGRAM_CYCLE_INVALID_WITHDRAWN_DEADLINE_DATE);
    }

    if (request.getGradeSubmissionDeadline() == null) {
      throw new ApplicationException(
          ProgramCycleMessage.PROGRAM_CYCLE_GRADES_SUBMISSION_DEADLINE_DATE_REQUIRED);
    }

    if (request.getGradeSubmissionDeadline().isAfter(request.getEndDate().atStartOfDay())) {
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
  }

  @Override
  protected void afterMakeUpdates(
      ProgramCycleDTO request, ProgramCycle beforeUpdates, ProgramCycle afterUpdates) {

    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (request.getStartDate() != null || request.getEndDate() != null) {
      if (afterUpdates.getStartDate().isAfter(afterUpdates.getEndDate())) {
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
      if (afterUpdates.getRegistrationStartDate().isAfter(afterUpdates.getRegistrationEndDate())) {
        throw new ApplicationException(ProgramCycleMessage.PROGRAM_CYCLE_INVALID_REGISTRATION_DATE);
      }
    }

    if (request.getWithdrawalDeadline() != null) {
      if (afterUpdates.getWithdrawalDeadline().isAfter(afterUpdates.getRegistrationEndDate())) {
        throw new ApplicationException(
            ProgramCycleMessage.PROGRAM_CYCLE_INVALID_WITHDRAWN_DEADLINE_DATE);
      }
    }

    if (request.getGradeSubmissionDeadline() != null) {
      if (afterUpdates
          .getGradeSubmissionDeadline()
          .isAfter(afterUpdates.getEndDate().atStartOfDay())) {
        throw new ApplicationException(
            ProgramCycleMessage.PROGRAM_CYCLE_INVALID_GRADES_SUBMISSION_DEADLINE_DATE);
      }
    }
  }
}
