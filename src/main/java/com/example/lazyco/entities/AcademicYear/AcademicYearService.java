package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.DateUtils.LocalDateRangeDTO;
import com.example.lazyco.core.Exceptions.ApplicationException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AcademicYearService extends CommonAbstractService<AcademicYearDTO, AcademicYear> {
  protected AcademicYearService(AcademicYearMapper academicYearMapper) {
    super(academicYearMapper);
  }

  @Override
  protected void addEntityFilters(CriteriaBuilderWrapper cbw, AcademicYearDTO filter) {
    if (filter.getDateComparison() != null) {
      switch (filter.getDateComparison()) {
        case DATE_BETWEEN_START_AND_END_DATE -> {
          cbw.and(
              cbw.getGePredicate("endDate", filter.getComparisonDate()),
              cbw.getLePredicate("startDate", filter.getComparisonDate()));
        }
        case CONFLICT_CHECK -> addConflictRestrictions(cbw, filter);
      }
    }
  }

  private void addConflictRestrictions(CriteriaBuilderWrapper cbw, AcademicYearDTO filter) {
    LocalDateRangeDTO newAcademicYearRange =
        new LocalDateRangeDTO(filter.getStartDateComparison(), filter.getEndDateComparison());
    cbw.addDateTimeRangeConflictCriteria(newAcademicYearRange, "startDate", "endDate");
  }

  @Override
  protected void validateBeforeCreate(AcademicYearDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(AcademicYearMessage.ACADEMIC_YEAR_CODE_REQUIRED);
    }
    // code should be unique
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(AcademicYearMessage.ACADEMIC_YEAR_NAME_REQUIRED);
    }
    // name should be unique
    validateUniqueName(requestDTO);

    // start date and end date conflicts check
    academicYearConflictCheck(requestDTO);
  }

  @Override
  protected void validateBeforeUpdate(AcademicYearDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO);
    }

    // start date and end date conflicts check
    academicYearConflictCheck(requestDTO);
  }

  private void academicYearConflictCheck(AcademicYearDTO requestDTO) {
    if (requestDTO.getStartDate() != null && requestDTO.getEndDate() != null) {
      AcademicYearDTO filter = new AcademicYearDTO();
      filter.setStartDateComparison(requestDTO.getStartDate());
      filter.setEndDateComparison(requestDTO.getEndDate());
      filter.setDateComparison(AcademicYearDTODateComparisonEnum.CONFLICT_CHECK);
      if (requestDTO.getId() != null) {
        filter.setIdsNotIn(List.of(requestDTO.getId()));
      }
      if (getCount(filter) > 0) {
        throw new ApplicationException(AcademicYearMessage.ACADEMIC_YEAR_DATE_CONFLICT);
      }
    }
  }
}
