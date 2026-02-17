package com.example.lazyco.entities.TermSystem;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.BatchException;
import com.example.lazyco.core.Messages.CustomMessage;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMaster;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMasterDTO;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMasterMapper;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMasterService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TermSystemService extends CommonAbstractService<TermSystemDTO, TermSystem> {

  private final TermMasterService termMasterService;
  private final TermMasterMapper termMasterMapper;

  protected TermSystemService(
      TermSystemMapper termSystemMapper,
      TermMasterService termMasterService,
      TermMasterMapper termMasterMapper) {
    super(termSystemMapper);
    this.termMasterService = termMasterService;
    this.termMasterMapper = termMasterMapper;
  }

  @Override
  protected void validateBeforeCreate(TermSystemDTO request) {
    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(TermSystemMessage.TERM_SYSTEM_CODE_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(TermSystemMessage.TERM_SYSTEM_NAME_REQUIRED);
    }
    validateUniqueName(request, TermSystemMessage.DUPLICATE_TERM_SYSTEM_NAME);
  }

  @Override
  protected void postCreate(
      TermSystemDTO request, TermSystem createdEntity, TermSystemDTO createdDTO) {
    // Create term masters associated with the term system
    mapTermMasters(request, createdEntity);
  }

  @Override
  protected void validateBeforeUpdate(TermSystemDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, TermSystemMessage.DUPLICATE_TERM_SYSTEM_NAME);
    }
  }

  @Override
  protected void postUpdate(
      TermSystemDTO request, TermSystemDTO entityBeforeUpdate, TermSystem updatedEntity) {
    // update term masters associated with the term system
    mapTermMasters(request, updatedEntity);
  }

  private void mapTermMasters(TermSystemDTO request, TermSystem entity) {
    boolean hasError = false;
    if (request.getTermMasters() != null && !request.getTermMasters().isEmpty()) {
      List<TermMasterDTO> termMasterDTOs = request.getTermMasters();

      List<TermMasterDTO> addTermMasterDTOs =
          termMasterDTOs.stream()
              .filter(dto -> dto.getId() == null)
              .peek(dto -> dto.setTermSystemId(entity.getId()))
              .toList();

      List<TermMasterDTO> updateTermMasterDTOs =
          termMasterDTOs.stream().filter(dto -> dto.getId() != null).toList();

      List<TermMasterDTO> resultTermMasterDTOs = new ArrayList<>();
      List<TermMaster> resultTermMasters = new ArrayList<>();

      if (!addTermMasterDTOs.isEmpty()) {
        TermMasterDTO termMasterDTO = new TermMasterDTO();
        termMasterDTO.setIsAtomicOperation(true);
        termMasterDTO.setObjects(addTermMasterDTOs);
        termMasterDTO = termMasterService.executeCreateNestedTransactional(termMasterDTO);
        resultTermMasterDTOs.addAll(termMasterDTO.getObjects());
        if (Boolean.TRUE.equals(termMasterDTO.getHasError())) {
          hasError = true;
        }
        List<TermMaster> createdTermMasters =
            termMasterMapper.mapDTOList(termMasterDTO.getObjects());
        resultTermMasters.addAll(createdTermMasters);
      }

      if (!updateTermMasterDTOs.isEmpty()) {
        TermMasterDTO termMasterDTO = new TermMasterDTO();
        termMasterDTO.setIsAtomicOperation(true);
        termMasterDTO.setObjects(updateTermMasterDTOs);
        termMasterDTO = termMasterService.executeUpdateNestedTransactional(termMasterDTO);
        resultTermMasterDTOs.addAll(termMasterDTO.getObjects());
        if (Boolean.TRUE.equals(termMasterDTO.getHasError())) {
          hasError = true;
        }
        List<TermMaster> updatedTermMasters =
            termMasterMapper.mapDTOList(termMasterDTO.getObjects());
        resultTermMasters.addAll(updatedTermMasters);
      }

      entity.setTermMasters(new HashSet<>(resultTermMasters));

      if (hasError) {
        request.setTermMasters(resultTermMasterDTOs);
        request.setMessage(
            CustomMessage.getMessageString(TermSystemMessage.ERROR_SAVING_TERM_MASTERS));
        throw new BatchException(HttpStatus.BAD_REQUEST, request);
      }
    }
  }
}
