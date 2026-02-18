package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.entities.AcademicProgram.AcademicProgram;
import com.example.lazyco.entities.AcademicProgram.AcademicProgramService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProgramCurriculumService
    extends CommonAbstractService<ProgramCurriculumDTO, ProgramCurriculum> {

  private final AcademicProgramService academicProgramService;

  protected ProgramCurriculumService(
      ProgramCurriculumMapper programCurriculumMapper,
      AcademicProgramService academicProgramService) {
    super(programCurriculumMapper);
    this.academicProgramService = academicProgramService;
  }

  @Override
  protected void validateBeforeCreate(ProgramCurriculumDTO request) {

    if (request.getAcademicYearId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_ACADEMIC_YEAR_REQUIRED);
    }

    if (request.getTermSystemId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_TERM_SYSTEM_REQUIRED);
    }

    if (request.getAcademicProgramId() == null) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_PROGRAM_REQUIRED);
    }

    if (request.getProgramTermSystemId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_TERM_CYCLE_REQUIRED);
    }

    validateAcademicProgramTermSystem(
        request.getAcademicProgramId(), request.getProgramTermSystemId());

    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_CODE_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_NAME_REQUIRED);
    }
    validateUniqueName(request, ProgramCurriculumMessage.DUPLICATE_PROGRAM_CURRICULUM_NAME);
  }

  @Override
  protected void preCreate(ProgramCurriculumDTO request, ProgramCurriculum entityToCreate) {
    // program curriculum validation
    programCurriculumValidation(entityToCreate);
  }

  @Override
  protected void validateBeforeUpdate(ProgramCurriculumDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, ProgramCurriculumMessage.DUPLICATE_PROGRAM_CURRICULUM_NAME);
    }
  }

  @Override
  protected void afterMakeUpdates(
      ProgramCurriculumDTO request,
      ProgramCurriculum beforeUpdates,
      ProgramCurriculum afterUpdates) {
    if (request.getAcademicProgramId() != null || request.getProgramTermSystemId() != null) {
      validateAcademicProgramTermSystem(
          afterUpdates.getAcademicProgram().getId(), afterUpdates.getProgramTermSystem().getId());
    }
  }

  @Override
  protected void preUpdate(
      ProgramCurriculumDTO request,
      ProgramCurriculumDTO entityBeforeUpdates,
      ProgramCurriculum entityToUpdate) {
    // program curriculum validation
    programCurriculumValidation(entityToUpdate);
  }

  private void programCurriculumValidation(ProgramCurriculum entity) {
    if (entity.getStartDate() == null
        || entity.getEndDate() == null
        || entity.getEndDate().isBefore(entity.getStartDate())) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_INVALID_DATES);
    }

    if (entity.getConvictionDate() != null
        && entity.getConvictionDate().isBefore(entity.getEndDate())) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_INVALID_CONVICTION_DATE);
    }

    if (entity.getAdmissionCapacity() != null && entity.getAdmissionCapacity() < 0) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_ADMISSION_CAPACITY_REQUIRED);
    }

    if (entity.getMinCredit() != null && entity.getMinCredit() < 0) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_MIN_CREDIT_REQUIRED);
    }

    if (entity.getMaxCredit() != null && entity.getMaxCredit() < 0) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_MAX_CREDIT_REQUIRED);
    }

    if (entity.getMinCredit() != null
        && entity.getMaxCredit() != null
        && entity.getMaxCredit() < entity.getMinCredit()) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_CREDIT_VALIDATION);
    }
  }

  private void validateAcademicProgramTermSystem(Long academicProgramId, Long programTermSystemId) {
    AcademicProgram academicProgram = academicProgramService.getEntityById(academicProgramId);
    if (academicProgram == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_ACADEMIC_PROGRAM_NOT_FOUND);
    }
    if (academicProgram.getProgramTermSystems().stream()
        .noneMatch(programTermSystem -> programTermSystem.getId().equals(programTermSystemId))) {
      throw new ApplicationException(
          ProgramCurriculumMessage
              .PROGRAM_CURRICULUM_PROGRAM_TERM_SYSTEM_NOT_ASSOCIATED_WITH_PROGRAM);
    }
  }
}
