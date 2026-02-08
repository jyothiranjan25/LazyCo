package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.entities.UserManagement.Module.ModuleDTO;
import com.example.lazyco.entities.UserManagement.Module.ModuleService;
import com.example.lazyco.entities.UserManagement.Resource.ResourceDTO;
import com.example.lazyco.entities.UserManagement.RoleModuleResource.RoleModuleResourceDTO;
import com.example.lazyco.entities.UserManagement.RoleModuleResource.RoleModuleResourceService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends CommonAbstractService<RoleDTO, Role> {

  private final ModuleService moduleService;
  private final RoleModuleResourceService roleModuleResourceService;

  protected RoleService(
      RoleMapper roleMapper,
      ModuleService moduleService,
      RoleModuleResourceService roleModuleResourceService) {
    super(roleMapper);
    this.moduleService = moduleService;
    this.roleModuleResourceService = roleModuleResourceService;
  }

  @Override
  protected List<RoleDTO> modifyGetResult(List<RoleDTO> result, RoleDTO filter) {
    for (RoleDTO roleDTO : result) {
      if (roleDTO.getRoleModuleResources() != null && !roleDTO.getRoleModuleResources().isEmpty()) {
        roleDTO.setRoleModuleResources(mapModuleResourceTree(roleDTO.getRoleModuleResources()));
      }
    }
    return result;
  }

  private List<RoleModuleResourceDTO> mapModuleResourceTree(
      List<RoleModuleResourceDTO> roleModuleResources) {
    // group by module id
    Map<Long, List<RoleModuleResourceDTO>> groupByModuleId =
        roleModuleResources.stream()
            .filter(r -> r.getModuleId() != null)
            .collect(Collectors.groupingBy(RoleModuleResourceDTO::getModuleId));

    List<RoleModuleResourceDTO> treeMap = new ArrayList<>();
    for (Map.Entry<Long, List<RoleModuleResourceDTO>> moduleEntry : groupByModuleId.entrySet()) {
      List<RoleModuleResourceDTO> moduleResources = moduleEntry.getValue();
      // group by resource id inside the module
      List<ResourceDTO> resourceDTOList =
          moduleResources.stream()
              .filter(r -> r.getResourceId() != null)
              .collect(
                  Collectors.toMap(
                      RoleModuleResourceDTO::getResourceId,
                      RoleModuleResourceDTO::getResource,
                      (existing, duplicate) -> existing // keep first
                      ))
              .values()
              .stream()
              .toList();

      // Map module resources
      ModuleDTO module = moduleResources.get(0).getModule();
      Integer moduleOrder = moduleResources.get(0).getDisplayOrder();
      module.setResources(moduleService.mapResourceTree(resourceDTOList));

      RoleModuleResourceDTO roleModuleResourceDTO = new RoleModuleResourceDTO();
      roleModuleResourceDTO.setModule(module);
      roleModuleResourceDTO.setDisplayOrder(moduleOrder);
      treeMap.add(roleModuleResourceDTO);
    }
    return treeMap;
  }

  @Override
  protected void validateBeforeCreate(RoleDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(RoleMessage.ROLE_NAME_REQUIRED);
    }

    // name should be unique
    validateUniqueName(requestDTO, RoleMessage.DUPLICATE_ROLE_NAME);
  }

  @Override
  protected void postCreate(RoleDTO requestDTO, Role createdEntity) {
    mapRoleModuleResources(requestDTO, createdEntity);
  }

  @Override
  protected void validateBeforeUpdate(RoleDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, RoleMessage.DUPLICATE_ROLE_NAME);
    }
  }

  @Override
  protected void postUpdate(RoleDTO requestDTO, RoleDTO entityBeforeUpdate, Role updatedEntity) {
    mapRoleModuleResources(requestDTO, updatedEntity);
  }

  public void mapRoleModuleResources(RoleDTO requestDTO, Role entity) {
    if (requestDTO.getRoleModuleResources() != null
        && !requestDTO.getRoleModuleResources().isEmpty()) {
      for (RoleModuleResourceDTO roleModuleResourceDTO : requestDTO.getRoleModuleResources()) {
        roleModuleResourceDTO.setRoleId(entity.getId());
        if (roleModuleResourceDTO.getId() == null) {
          roleModuleResourceService.executeCreateTransactional(roleModuleResourceDTO);
        } else {
          roleModuleResourceService.executeDeleteTransactional(roleModuleResourceDTO);
        }
      }
    }
  }
}
