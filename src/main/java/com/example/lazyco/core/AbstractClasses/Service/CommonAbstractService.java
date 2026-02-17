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

  public void validateUniqueCode(D incomingRequest) {
    this.validateUniqueCode(incomingRequest, null);
  }

  public void validateUniqueCode(D incomingRequest, MessageCodes messageCodes) {
    if (!HasCode.class.isAssignableFrom(incomingRequest.getClass())) {
      throw new ApplicationException(CommonMessage.ERROR_VALIDATING_CODE);
    }
    abstractAction.pushBypassRBAC(true);
    try {
      D filter = getDtoClass().getDeclaredConstructor().newInstance();
      HasCode filterWithCode = (HasCode) filter;
      String filterCode = ((HasCode) incomingRequest).getCode();

      if (filterCode == null || filterCode.trim().isEmpty()) {
        throw new ApplicationException(CommonMessage.CODE_REQUIRED);
      }

      filterWithCode.setCode(filterCode);

      if (incomingRequest.getId() != null) {
        filter.setIdsNotIn(List.of(((HasCode) incomingRequest).getId()));
      }

      if (getCount(filter) > 0) {
        if (messageCodes == null) {
          messageCodes = CommonMessage.CODE_ALREADY_EXISTS;
        }
        throw new ApplicationException(messageCodes, new Object[] {filterCode});
      }
    } catch (ApplicationException e) {
      throw e; // Rethrow application exceptions to be handled by the caller
    } catch (Exception e) {
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    } finally {
      abstractAction.popBypassRBAC();
    }
  }

  public void validateUniqueName(D incomingRequest) {
    this.validateUniqueName(incomingRequest, null);
  }

  public void validateUniqueName(D incomingRequest, MessageCodes messageCodes) {
    if (!HasName.class.isAssignableFrom(incomingRequest.getClass())) {
      throw new ApplicationException(CommonMessage.ERROR_VALIDATING_NAME);
    }
    try {
      D filter = getDtoClass().getDeclaredConstructor().newInstance();
      HasName filterWithName = (HasName) filter;
      String filterName = ((HasName) incomingRequest).getName();

      if (filterName == null || filterName.trim().isEmpty()) {
        throw new ApplicationException(CommonMessage.NAME_REQUIRED);
      }

      filterWithName.setName(filterName);

      if (incomingRequest.getId() != null) {
        filter.setIdsNotIn(List.of(((HasName) incomingRequest).getId()));
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
  public <T> void addEntities(Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (incomingCollection == null || existingCollection == null) {
      return;
    }

    for (T entity : incomingCollection) {
      if (!existingCollection.contains(entity)) {
        existingCollection.add(entity);
      }
    }
  }

  public <T> void removeEntities(
      Collection<T> existingCollection, Collection<T> incomingCollection) {

    if (incomingCollection == null || existingCollection == null) {
      return;
    }

    for (T entity : incomingCollection) {
      existingCollection.remove(entity);
    }
  }

  public <T extends AbstractModel, O extends AbstractDTO<O>> void removeAssociated(
      Collection<T> existingCollection, Collection<O> incomingCollection) {

    if (incomingCollection == null || existingCollection == null) {
      return;
    }

    for (O incomingRequest : incomingCollection) {
      if (incomingRequest.getId() == null) {
        continue; // Skip if incoming DTO doesn't have an ID
      }
      existingCollection.removeIf(entity -> entity.getId().equals(incomingRequest.getId()));
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
