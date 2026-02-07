package com.example.lazyco.core.AbstractClasses.Service;

import com.example.lazyco.core.AbstractAction;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCode;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import com.example.lazyco.core.Messages.MessageCodes;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class CommonAbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    extends AbstractService<D, E> {

  protected CommonAbstractService(AbstractMapper<D, E> abstractMapper) {
    super(abstractMapper);
  }

  private AbstractAction abstractAction;

  @Autowired
  public void injectDependencies(AbstractAction abstractAction) {
    this.abstractAction = abstractAction;
  }

  public void validateUniqueCode(D incomingDto) {
    this.validateUniqueCode(incomingDto, null);
  }

  public void validateUniqueCode(D incomingDto, MessageCodes messageCodes) {
    if (!HasCode.class.isAssignableFrom(incomingDto.getClass())) {
      throw new ApplicationException(CommonMessage.ERROR_VALIDATING_CODE);
    }
    abstractAction.setBypassRBAC(true);
    try {
      D filter = getDtoClass().getDeclaredConstructor().newInstance();
      HasCode filterWithCode = (HasCode) filter;
      String filterCode = ((HasCode) incomingDto).getCode();

      if (filterCode == null || filterCode.trim().isEmpty()) {
        throw new ApplicationException(CommonMessage.CODE_REQUIRED);
      }

      filterWithCode.setCode(filterCode);

      if (incomingDto.getId() != null) {
        filter.setIdsNotIn(List.of(((HasCode) incomingDto).getId()));
      }

      if (getCount(filter) > 0) {
        if (messageCodes == null) {
          messageCodes = CommonMessage.CODE_ALREADY_EXISTS;
        }
        throw new ApplicationException(messageCodes);
      }
    } catch (ApplicationException e) {
      throw e; // Rethrow application exceptions to be handled by the caller
    } catch (Exception e) {
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    } finally {
      abstractAction.setBypassRBAC(false);
    }
  }

  public void validateUniqueName(D incomingDto) {
    this.validateUniqueName(incomingDto, null);
  }

  public void validateUniqueName(D incomingDto, MessageCodes messageCodes) {
    if (!HasName.class.isAssignableFrom(incomingDto.getClass())) {
      throw new ApplicationException(CommonMessage.ERROR_VALIDATING_NAME);
    }
    try {
      D filter = getDtoClass().getDeclaredConstructor().newInstance();
      HasName filterWithName = (HasName) filter;
      String filterName = ((HasName) incomingDto).getName();

      if (filterName == null || filterName.trim().isEmpty()) {
        throw new ApplicationException(CommonMessage.NAME_REQUIRED);
      }

      filterWithName.setName(filterName);

      if (incomingDto.getId() != null) {
        filter.setIdsNotIn(List.of(((HasName) incomingDto).getId()));
      }

      if (getCount(filter) > 0) {
        if (messageCodes == null) {
          messageCodes = CommonMessage.DUPLICATE_FIELD;
        }
        throw new ApplicationException(messageCodes, new Object[] {filterName});
      }
    } catch (ApplicationException e) {
      throw e; // Rethrow application exceptions to be handled by the caller
    } catch (Exception e) {
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Utility methods for managing associated entities in collections
  public <T> void addAssociated(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (incomingCollection == null || existingCollection == null) {
      return;
    }

    for (T entity : incomingCollection) {
      if (!existingCollection.contains(entity)) {
        existingCollection.add(entity);
      }
    }
  }

  public <T> void removeAssociated(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (incomingCollection == null || existingCollection == null) {
      return;
    }

    for (T entity : incomingCollection) {
      existingCollection.remove(entity);
    }
  }

  public <T> Collection<T> addAssociatedEntities(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (existingCollection == null) {
      existingCollection = new HashSet<>();
    }

    if (incomingCollection != null) {
      for (T entity : incomingCollection) {
        if (!existingCollection.contains(entity)) {
          existingCollection.add(entity);
        }
      }
    }
    return existingCollection;
  }

  public <T> Collection<T> removeAssociatedEntities(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (existingCollection == null) {
      existingCollection = new HashSet<>();
    }

    if (incomingCollection != null) {
      for (T entity : incomingCollection) {
        existingCollection.remove(entity);
      }
    }
    return existingCollection;
  }
}
