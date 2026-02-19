package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.OrConditionDTO;
import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.entities.Student.StudentDTO;
import com.example.lazyco.entities.Student.StudentMessage;
import com.example.lazyco.entities.Student.StudentService;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdmissionService extends CommonAbstractService<AdmissionDTO, Admission> {
  private final AdmissionToStudentMapper admissionToStudentMapper;
  private final StudentService studentService;

  protected AdmissionService(
      AdmissionMapper admissionMapper,
      AdmissionToStudentMapper admissionToStudentMapper,
      StudentService studentService) {
    super(admissionMapper);
    this.admissionToStudentMapper = admissionToStudentMapper;
    this.studentService = studentService;
  }

  @Transactional
  protected void validateBeforeCreate(AdmissionDTO request) {
    // Admission Mandatory Fields Validation
    if (StringUtils.isEmpty(request.getAdmissionNumber())) {
      throw new ApplicationException(AdmissionMessage.ADMISSION_NUMBER_IS_REQUIRED);
    }
    if (request.getProgramCurriculumId() == null) {
      throw new ApplicationException(AdmissionMessage.PROGRAM_CURRICULUM_REQUIRED);
    }
    if (request.getJoiningProgramCycleId() == null) {
      throw new ApplicationException(AdmissionMessage.JOINING_PROGRAM_CYCLE_REQUIRED);
    } else {
      request.setCurrentProgramCycleId(request.getJoiningProgramCycleId());
    }

    // Student Mandatory Fields Validation
    if (StringUtils.isEmpty(request.getFirstName())) {
      throw new ApplicationException(StudentMessage.FIRST_NAME_IS_REQUIRED);
    }
    if (StringUtils.isEmpty(request.getEmail())) {
      throw new ApplicationException(StudentMessage.EMAIL_IS_REQUIRED);
    }
    if (StringUtils.isEmpty(request.getPhoneNumber())) {
      throw new ApplicationException(StudentMessage.PHONE_NUMBER_IS_REQUIRED);
    }

    // check if student already exists based on email or phone number
    StudentDTO studentDTO = getOrCreateStudent(request);
    if (studentDTO != null) {
      request.setStudentId(studentDTO.getId());
      // if student exists, validate the admission for the curriculum
      validateAdmissionForCurriculum(request);
    } else {
      // if student does not exist, create a new student
      studentDTO = admissionToStudentMapper.map(request);
      studentDTO = studentService.executeCreateTransactional(studentDTO);
      request = admissionToStudentMapper.map(studentDTO, request);
    }
  }

  private StudentDTO getOrCreateStudent(AdmissionDTO request) {
    StudentDTO studentDTO = new StudentDTO();
    studentDTO.setOrConditions(
        Set.of(
            new OrConditionDTO("email", request.getEmail()),
            new OrConditionDTO("phoneNumber", request.getPhoneNumber())));
    return studentService.getSingle(studentDTO);
  }

  private void validateAdmissionForCurriculum(AdmissionDTO request) {
    AdmissionDTO admissionDTO = new AdmissionDTO();
    admissionDTO.setStudentId(request.getStudentId());
    admissionDTO.setProgramCurriculumId(request.getProgramCurriculumId());
    if (getCount(admissionDTO) > 0) {
      throw new ApplicationException(AdmissionMessage.ADMISSION_ALREADY_EXISTS_FOR_CURRICULUM);
    }
  }
}
