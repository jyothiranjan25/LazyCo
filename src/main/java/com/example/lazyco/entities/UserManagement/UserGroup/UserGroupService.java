package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractAction;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService extends CommonAbstractService<UserGroupDTO, UserGroup> {

  private final AbstractAction abstractAction;

  protected UserGroupService(UserGroupMapper UserGroupMapper, AbstractAction abstractAction) {
    super(UserGroupMapper);
    this.abstractAction = abstractAction;
  }

  @Override
  protected UserGroupDTO updateFilterBeforeGet(UserGroupDTO filter) {
    // extract logged in user group id and set it to filter to fetch only the user groups relevant
    // to logged in user
    if (Boolean.TRUE.equals(filter.getFetchForLoggedInUser())) {
      UserGroupDTO userGroupDTO = abstractAction.getLoggedInUserGroup();
      if (userGroupDTO != null && userGroupDTO.getId() != null) {
        filter.setId(userGroupDTO.getId());
      }
    }
    return filter;
  }

  @Override
  protected void addEntityFilters(CriteriaBuilderWrapper cbw, UserGroupDTO filter) {
    if (Boolean.TRUE.equals(filter.getFetchParent())) {
      cbw.join("parentUserGroup");
      cbw.isNull("parentUserGroup.id");
    }
  }

  @Override
  protected void validateBeforeCreate(UserGroupDTO requestDTO) {
    // name is required
    if (StringUtils.isEmpty(requestDTO.getUserGroupName())) {
      throw new ApplicationException(UserGroupMessage.USER_GROUP_NAME_REQUIRED);
    }

    // name should be unique
    validateUniqueName(requestDTO, UserGroupMessage.DUPLICATE_USER_GROUP_NAME);

    // set fully qualified name
    setFullyQualifiedName(requestDTO);
  }

  private void setFullyQualifiedName(UserGroupDTO requestDTO) {
    StringBuilder qualifiedName = new StringBuilder(requestDTO.getUserGroupName());
    Long parentId = requestDTO.getParentId();
    while (parentId != null) {
      UserGroup parentUserGroup = getEntityById(parentId);
      if (parentUserGroup == null) {
        break;
      }
      qualifiedName.insert(0, parentUserGroup.getUserGroupName() + ".");
      parentId =
          parentUserGroup.getParentUserGroup() != null
              ? parentUserGroup.getParentUserGroup().getId()
              : null;
    }
    requestDTO.setFullyQualifiedName(qualifiedName.toString());
  }

  @Override
  protected void makeUpdates(UserGroupDTO source, UserGroup target) {
    if (source.getDescription() != null) {
      target.setDescription(source.getDescription());
    }
  }

  @Override
  protected void preDelete(UserGroupDTO requestDTO, UserGroup entityToDelete) {
    throw new ApplicationException(
        UserGroupMessage.USER_GROUP_DELETE_NOT_ALLOWED,
        new Object[] {entityToDelete.getUserGroupName()});
  }
}
