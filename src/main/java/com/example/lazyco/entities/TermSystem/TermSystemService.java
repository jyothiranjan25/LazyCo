package com.example.lazyco.entities.TermSystem;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.BatchException;
import com.example.lazyco.core.Messages.CustomMessage;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMaster;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMasterDTO;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMasterMapper;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMasterService;
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
  protected void validateBeforeCreate(TermSystemDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getCode())) {
      throw new ApplicationException(TermSystemMessage.TERM_SYSTEM_CODE_REQUIRED);
    }
    validateUniqueCode(requestDTO);

    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(TermSystemMessage.TERM_SYSTEM_NAME_REQUIRED);
    }
    validateUniqueName(requestDTO, TermSystemMessage.DUPLICATE_TERM_SYSTEM_NAME);

    // check if term master exists
    if (requestDTO.getTermMasters() == null || requestDTO.getTermMasters().isEmpty()) {
      throw new ApplicationException(TermSystemMessage.TERM_SYSTEM_TERM_MASTERS_REQUIRED);
    }
  }

  @Override
  protected void postCreate(TermSystemDTO requestDTO, TermSystem createdEntity) {
    // Create term masters associated with the term system
    List<TermMasterDTO> termMasterDTOs = requestDTO.getTermMasters();
    for (TermMasterDTO termMasterDTO : termMasterDTOs) {
      termMasterDTO.setTermSystemId(createdEntity.getId());
    }
    TermMasterDTO termMasterDTO = new TermMasterDTO();
    termMasterDTO.setIsAtomicOperation(true);
    termMasterDTO.setObjects(termMasterDTOs);
    termMasterDTO = termMasterService.executeCreateNestedTransactional(termMasterDTO,false);
    requestDTO.setTermMasters(termMasterDTO.getObjects());
    if (Boolean.TRUE.equals(termMasterDTO.getHasError())) {
      requestDTO.setMessage(
          CustomMessage.getMessageString(TermSystemMessage.ERROR_CREATING_TERM_MASTERS));
      throw new BatchException(HttpStatus.BAD_REQUEST, requestDTO);
    }
    List<TermMaster> createdTermMasters = termMasterMapper.mapDTOList(termMasterDTO.getObjects());
    createdEntity.setTermMasters(new HashSet<>(createdTermMasters));
  }

  @Override
  protected void validateBeforeUpdate(TermSystemDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getCode())) {
      validateUniqueCode(requestDTO);
    }

    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, TermSystemMessage.DUPLICATE_TERM_SYSTEM_NAME);
    }
  }
}
