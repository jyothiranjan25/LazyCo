package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.entities.UserManagement.Resource.Resource;
import com.example.lazyco.entities.UserManagement.Resource.ResourceDTO;
import com.example.lazyco.entities.UserManagement.Resource.ResourceMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModuleService extends AbstractService<ModuleDTO, Module> {

  private final ResourceMapper resourceMapper;

  protected ModuleService(ModuleMapper moduleMapper, ResourceMapper resourceMapper) {
    super(moduleMapper);
    this.resourceMapper = resourceMapper;
  }

  @Override
  protected List<ModuleDTO> modifyGetResult(List<ModuleDTO> result, ModuleDTO filter) {
    for (ModuleDTO moduleDTO : result) {
      if (moduleDTO.getResources() != null && !moduleDTO.getResources().isEmpty()) {
        moduleDTO.setResources(mapResourceTree(moduleDTO.getResources()));
      }
    }
    return result;
  }

  private List<ResourceDTO> mapResourceTree(List<ResourceDTO> resources) {
    // group resources by parentId
    Map<Long, List<ResourceDTO>> groupedByParent =
        resources.stream()
            .filter(r -> r.getParentId() != null)
            .collect(Collectors.groupingBy(ResourceDTO::getParentId));
    // now get the root resources (those with no parent)
    List<ResourceDTO> rootResources =
        resources.stream().filter(r -> r.getParentId() == null).toList();
    // recursively set children
    for (ResourceDTO root : rootResources) {
      List<ResourceDTO> children = groupedByParent.get(root.getId());
      if (children != null && !children.isEmpty()) {
        root.setChildResources(children);
      }
    }
    return rootResources;
  }

  @Override
  protected void validateBeforeCreate(ModuleDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getModuleName())) {
      throw new ApplicationException(ModuleMessage.MODULE_NAME_REQUIRED);
    }
    validateDuplicateName(requestDTO.getModuleName(), null);
  }

  @Override
  protected void preCreate(ModuleDTO requestDTO, Module entityToCreate) {
    mapAssociatedEntities(requestDTO, entityToCreate);
  }

  @Override
  protected ModuleDTO modifyCreateResult(ModuleDTO requestDTO, ModuleDTO createdDTO) {
    if (createdDTO.getResources() != null && !createdDTO.getResources().isEmpty()) {
      createdDTO.setResources(mapResourceTree(createdDTO.getResources()));
    }
    if (true) {
      throw new ExceptionWrapper("Simulated exception after creation");
    }
    return createdDTO;
  }

  @Override
  protected void validateBeforeUpdate(ModuleDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getModuleName())) {
      validateDuplicateName(requestDTO.getModuleName(), requestDTO.getId());
    }
  }

  @Override
  protected void makeUpdates(ModuleDTO source, Module target) {
    super.makeUpdates(source, target);
    mapAssociatedEntities(source, target);
  }

  private void mapAssociatedEntities(ModuleDTO source, Module target) {
    if (source.getAddResources() != null && !source.getAddResources().isEmpty()) {
      List<Resource> addResources = resourceMapper.mapDTOList(source.getAddResources());
      addAssociatedEntities(target.getResources(), addResources);
    }
    if (source.getRemoveResources() != null && !source.getRemoveResources().isEmpty()) {
      List<Resource> removeResources = resourceMapper.mapDTOList(source.getRemoveResources());
      removeAssociatedEntities(target.getResources(), removeResources);
    }
  }

  private void validateDuplicateName(String moduleName, Long excludeId) {
    ModuleDTO filter = new ModuleDTO();
    filter.setModuleName(moduleName);
    if (excludeId != null) {
      filter.setIdsNotIn(List.of(excludeId));
    }
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          ModuleMessage.DUPLICATE_MODULE_NAME, new Object[] {moduleName});
    }
  }

  @Override
  protected ModuleDTO modifyUpdateResult(ModuleDTO requestDTO, ModuleDTO updatedDTO) {
    if (updatedDTO.getResources() != null && !updatedDTO.getResources().isEmpty()) {
      updatedDTO.setResources(mapResourceTree(updatedDTO.getResources()));
    }
    return updatedDTO;
  }
}
