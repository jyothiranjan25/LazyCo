package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends CommonAbstractService<ResourceDTO, Resource> {
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
  protected void validateBeforeCreate(ResourceDTO request) {
    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(ResourceMessage.RESOURCE_NAME_REQUIRED);
    }
    // name should be unique
    validateUniqueName(request, ResourceMessage.DUPLICATE_RESOURCE_NAME);

    // validate that child should not have children
    validateParentResource(request);
  }

  private void validateParentResource(ResourceDTO request) {
    if (request.getParentId() != null) {
      ResourceDTO parentResource = getById(request.getParentId());
      if (parentResource.getParentId() != null) {
        throw new ApplicationException(ResourceMessage.INVALID_PARENT_RESOURCE);
      }
    }
  }

  @Override
  protected void validateBeforeUpdate(ResourceDTO request) {
    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, ResourceMessage.DUPLICATE_RESOURCE_NAME);
    }
  }
}
