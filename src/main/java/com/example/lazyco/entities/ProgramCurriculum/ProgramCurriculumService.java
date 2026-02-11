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
  protected void validateBeforeCreate(ProgramCurriculumDTO requestDTO) {

    if (requestDTO.getAcademicYearId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_ACADEMIC_YEAR_REQUIRED);
    }

    if (requestDTO.getTermSystemId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_TERM_SYSTEM_REQUIRED);
    }

    if (requestDTO.getAcademicProgramId() == null) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_PROGRAM_REQUIRED);
    }

    if (requestDTO.getProgramTermSystemId() == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_TERM_CYCLE_REQUIRED);
    }

    validateAcademicProgramTermSystem(
        requestDTO.getAcademicProgramId(), requestDTO.getProgramTermSystemId());

    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_CODE_REQUIRED);
    }
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(ProgramCurriculumMessage.PROGRAM_CURRICULUM_NAME_REQUIRED);
    }
    validateUniqueName(requestDTO, ProgramCurriculumMessage.DUPLICATE_PROGRAM_CURRICULUM_NAME);
  }

  @Override
  protected void preCreate(ProgramCurriculumDTO requestDTO, ProgramCurriculum entityToCreate) {
    // program curriculum validation
    programCurriculumValidation(entityToCreate);
  }

  @Override
  protected void validateBeforeUpdate(ProgramCurriculumDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, ProgramCurriculumMessage.DUPLICATE_PROGRAM_CURRICULUM_NAME);
    }
  }

  @Override
  protected void makeUpdates(ProgramCurriculumDTO source, ProgramCurriculum target) {
    if ((source.getAcademicProgramId() != null
            && target.getAcademicProgram() != null
            && !target.getAcademicProgram().getId().equals(source.getAcademicProgramId()))
        || (source.getProgramTermSystemId() != null
            && target.getProgramTermSystem() != null
            && !target.getProgramTermSystem().getId().equals(source.getProgramTermSystemId()))) {

      Long academicProgramId =
          source.getAcademicProgramId() != null
              ? source.getAcademicProgramId()
              : target.getAcademicProgram().getId();

      Long programTermSystemId =
          source.getProgramTermSystemId() != null
              ? source.getProgramTermSystemId()
              : target.getProgramTermSystem().getId();

      validateAcademicProgramTermSystem(academicProgramId, programTermSystemId);
    }

    super.makeUpdates(source, target);
  }

  @Override
  protected void preUpdate(
      ProgramCurriculumDTO requestDTO,
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
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_MAX_CREDIT_SHOULD_BE_GREATER_THAN_MIN_CREDIT);
    }
  }

  private void validateAcademicProgramTermSystem(Long academicProgramId, Long programTermSystemId) {
    AcademicProgram academicProgram = academicProgramService.getEntityById(academicProgramId);
    if (academicProgram == null) {
      throw new ApplicationException(
          ProgramCurriculumMessage.PROGRAM_CURRICULUM_INVALID_ACADEMIC_PROGRAM);
    }
    if (academicProgram.getProgramTermSystems().stream()
        .noneMatch(programTermSystem -> programTermSystem.getId().equals(programTermSystemId))) {
      throw new ApplicationException(
          ProgramCurriculumMessage
              .PROGRAM_CURRICULUM_PROGRAM_TERM_SYSTEM_NOT_ASSOCIATED_WITH_PROGRAM);
    }
  }
}
