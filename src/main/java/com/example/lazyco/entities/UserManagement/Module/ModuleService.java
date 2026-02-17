package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.entities.UserManagement.Resource.Resource;
import com.example.lazyco.entities.UserManagement.Resource.ResourceDTO;
import com.example.lazyco.entities.UserManagement.Resource.ResourceMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModuleService extends CommonAbstractService<ModuleDTO, Module> {

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

  public List<ResourceDTO> mapResourceTree(List<ResourceDTO> resources) {
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
  protected void validateBeforeCreate(ModuleDTO request) {
    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(ModuleMessage.MODULE_NAME_REQUIRED);
    }

    // name should be unique
    validateUniqueName(request, ModuleMessage.DUPLICATE_MODULE_NAME);
  }

  @Override
  protected void preCreate(ModuleDTO request, Module entityToCreate) {
    mapAssociatedEntities(request, entityToCreate);
  }

  @Override
  protected ModuleDTO modifyCreateResult(ModuleDTO request, ModuleDTO createdDTO) {
    if (createdDTO.getResources() != null && !createdDTO.getResources().isEmpty()) {
      createdDTO.setResources(mapResourceTree(createdDTO.getResources()));
    }
    return createdDTO;
  }

  @Override
  protected void validateBeforeUpdate(ModuleDTO request) {
    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, ModuleMessage.DUPLICATE_MODULE_NAME);
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
      target.setResources(
          new HashSet<>(addAssociatedEntities(target.getResources(), addResources)));
    }
    if (source.getRemoveResources() != null && !source.getRemoveResources().isEmpty()) {
      List<Resource> removeResources = resourceMapper.mapDTOList(source.getRemoveResources());
      target.setResources(
          new HashSet<>(removeAssociatedEntities(target.getResources(), removeResources)));
    }
  }

  @Override
  protected ModuleDTO modifyUpdateResult(ModuleDTO request, ModuleDTO updatedDTO) {
    if (updatedDTO.getResources() != null && !updatedDTO.getResources().isEmpty()) {
      updatedDTO.setResources(mapResourceTree(updatedDTO.getResources()));
    }
    return updatedDTO;
  }
}
