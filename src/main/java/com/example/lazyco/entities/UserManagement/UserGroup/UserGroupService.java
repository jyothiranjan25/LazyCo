package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService extends AbstractService<UserGroupDTO, UserGroup> {
  protected UserGroupService(UserGroupMapper UserGroupMapper) {
    super(UserGroupMapper);
  }

  @Override
  protected void addEntityFilters(CriteriaBuilderWrapper cbw, UserGroupDTO filter) {
    if(Boolean.TRUE.equals(filter.getFetchParent())){
      cbw.join("parentUserGroup");
      cbw.isNull("parentUserGroup.id");
    }
  }

  @Override
  protected void updateDtoBeforeCreate(UserGroupDTO requestDTO) {
    verifyUserGroupCreate(requestDTO);
    StringBuilder qualifiedName = new StringBuilder(requestDTO.getUserGroupName());
    Long parentId = requestDTO.getParentId();
    while (parentId != null) {
      UserGroup parentUserGroup = getEntityById(parentId);
      if (parentUserGroup == null) {
        break;
      }
      qualifiedName.insert(0, parentUserGroup.getUserGroupName() + ".");
      parentId = parentUserGroup.getParentUserGroup() != null ? parentUserGroup.getParentUserGroup().getId() : null;
    }
    requestDTO.setFullyQualifiedName(qualifiedName.toString());
  }

  private void verifyUserGroupCreate(UserGroupDTO requestDTO) {
    if(requestDTO.getUserGroupName() == null || requestDTO.getUserGroupName().isEmpty()){
      throw new ApplicationException(UserGroupMessage.USER_GROUP_NAME_REQUIRED);
    }
    // check for duplicate name
    UserGroupDTO userGroupDTO = new UserGroupDTO();
    userGroupDTO.setUserGroupName(requestDTO.getUserGroupName());
    if(getCount(userGroupDTO) > 0){
        throw new ApplicationException(UserGroupMessage.DUPLICATE_USER_GROUP_NAME, new Object[]{requestDTO.getUserGroupName()});
    }
  }

  @Override
  protected void makeUpdates(UserGroupDTO source, UserGroup target) {
    if (source.getDescription() != null) {
      target.setDescription(source.getDescription());
    }
  }

  @Override
  protected void preDelete(UserGroupDTO requestDTO, UserGroup entityToDelete) {
    throw  new ApplicationException(UserGroupMessage.USER_GROUP_DELETE_NOT_ALLOWED,new  Object[]{entityToDelete.getUserGroupName()});
  }
}
