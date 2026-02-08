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
  protected void validateBeforeCreate(ResourceDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(ResourceMessage.RESOURCE_NAME_REQUIRED);
    }
    // name should be unique
    validateUniqueName(requestDTO, ResourceMessage.DUPLICATE_RESOURCE_NAME);

    // validate that child should not have children
    validateParentResource(requestDTO);
  }

  private void validateParentResource(ResourceDTO requestDTO) {
    if (requestDTO.getParentId() != null) {
      ResourceDTO parentResource = getById(requestDTO.getParentId());
      if (parentResource.getParentId() != null) {
        throw new ApplicationException(ResourceMessage.INVALID_PARENT_RESOURCE);
      }
    }
  }

  @Override
  protected void validateBeforeUpdate(ResourceDTO requestDTO) {
    if (!StringUtils.isEmpty(requestDTO.getName())) {
      validateUniqueName(requestDTO, ResourceMessage.DUPLICATE_RESOURCE_NAME);
    }
  }
}
