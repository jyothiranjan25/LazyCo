package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends AbstractService<ResourceDTO, Resource> {
  protected ResourceService(ResourceMapper resourceMapper) {
    super(resourceMapper);
  }

  @Override
  protected void addEntityFilters(CriteriaBuilderWrapper cbw, ResourceDTO filter) {
    if (Boolean.TRUE.equals(filter.getFetchParent())) {
      cbw.join("parentResource");
      cbw.isNull("parentResource.id");
    }
  }

  @Override
  protected void validateBeforeCreate(ResourceDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getResourceName())) {
      throw new ApplicationException(ResourceMessage.RESOURCE_NAME_REQUIRED);
    }
    validateDuplicateName(requestDTO.getResourceName(), null);
  }

  @Override
  protected void validateBeforeUpdate(ResourceDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getResourceName())) {
      validateDuplicateName(requestDTO.getResourceName(), requestDTO.getId());
    }
  }

  // Validate duplicate resource name
  private void validateDuplicateName(String resourceName, Long excludeId) {
    ResourceDTO filter = new ResourceDTO();
    filter.setResourceName(resourceName);

    if (excludeId != null) {
      filter.setIdsNotIn(List.of(excludeId));
    }

    if (getCount(filter) > 0) {
      throw new ApplicationException(
          ResourceMessage.DUPLICATE_RESOURCE_NAME, new Object[] {resourceName});
    }
  }
}
