package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.entities.UserManagement.Module.ModuleDTO;
import com.example.lazyco.entities.UserManagement.Module.ModuleService;
import com.example.lazyco.entities.UserManagement.Resource.ResourceDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleModuleResourceService
    extends AbstractService<RoleModuleResourceDTO, RoleModuleResource> {

  private final ModuleService moduleService;

  protected RoleModuleResourceService(
      RoleModuleResourceMapper roleModuleResourceMapper, ModuleService moduleService) {
    super(roleModuleResourceMapper);
    this.moduleService = moduleService;
  }

  @Override
  protected List<RoleModuleResourceDTO> modifyGetResult(
      List<RoleModuleResourceDTO> result, RoleModuleResourceDTO filter) {
    return mapModuleResourceTree(result);
  }

  public List<RoleModuleResourceDTO> mapModuleResourceTree(
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
  public RoleModuleResourceDTO executeCreateTransactional(RoleModuleResourceDTO dto) {
    RoleModuleResourceDTO filter = new RoleModuleResourceDTO();
    filter.setRoleId(dto.getRoleId());
    filter.setModuleId(dto.getModuleId());
    filter.setResourceId(dto.getResourceId());

    if (getCount(filter) < 1) {
      return super.executeCreateTransactional(dto);
    }
    return dto;
  }

  @Transactional
  public void directUpdate(RoleModuleResourceDTO dto) {
    RoleModuleResourceDTO filter = new RoleModuleResourceDTO();
    filter.setRoleId(dto.getRoleId());
    filter.setModuleId(dto.getModuleId());
    filter.setResourceId(dto.getResourceId());
    updateMethod(dto, filter);
  }

  @Transactional
  public void directDelete(RoleModuleResourceDTO dto) {
    RoleModuleResourceDTO filter = new RoleModuleResourceDTO();
    filter.setRoleId(dto.getRoleId());
    filter.setModuleId(dto.getModuleId());
    filter.setResourceId(dto.getResourceId());
    deleteMethod(filter);
  }
}
