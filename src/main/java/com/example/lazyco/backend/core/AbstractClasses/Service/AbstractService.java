package com.example.lazyco.backend.core.AbstractClasses.Service;

import com.example.lazyco.backend.core.AbstractClasses.DAO.IAbstractDAO;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractClasses.JpaRepository.AbstractJpaRepository;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.CriteriaBuilder.CriteriaBuilderWrapper;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Transactional
public abstract class AbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    implements IAbstractService<D, E> {

  @Autowired
  @Lazy
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private AbstractService<D, E> self;

  private final AbstractMapper<D, E> abstractMapper;
  private final AbstractJpaRepository<E> abstractJpaRepository;
  private IAbstractDAO<D, E> abstractDAO;

  @Getter private final Class<D> dtoClass;

  protected AbstractService(
      AbstractMapper<D, E> abstractMapper, AbstractJpaRepository<E> abstractJpaRepository) {
    this.abstractMapper = abstractMapper;
    this.abstractJpaRepository = abstractJpaRepository;
    dtoClass = this.calculateDTOClass();
  }

  @Autowired
  private void injectDependencies(IAbstractDAO<D, E> abstractDAO) {
    this.abstractDAO = abstractDAO;
  }

  @SuppressWarnings("unchecked")
  private Class<D> calculateDTOClass() {
    Type superClass = getClass().getGenericSuperclass();
    if (superClass instanceof ParameterizedType parameterizedType) {
      Type type = parameterizedType.getActualTypeArguments()[0];
      if (type instanceof Class) {
        return (Class<D>) type;
      } else if (type instanceof ParameterizedType) {
        return (Class<D>) ((ParameterizedType) type).getRawType();
      }
    }
    throw new IllegalArgumentException("Could not determine DTO class");
  }

  // Do not call this method directly, use the template method instead
  public D create(D dto) {
    return ServiceOperationTemplate.executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoToCreate) {
            if (!Boolean.TRUE.equals(dto.getIsAtomicOperation()))
              return self.executeCreateTransactional(dtoToCreate);
            else return executeCreateNestedTransactional(dtoToCreate);
          }
        },
        dto);
  }

  // Execute create in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeCreateTransactional(D dto) {
    return executeCreate(dto);
  }

  // Execute create in the current transaction
  public D executeCreateNestedTransactional(D dto) {
    return executeCreate(dto);
  }

  // Core create logic
  private D executeCreate(D dtoToCreate) {
    // Hook for subclasses to modify dto before creation
    updateDtoBeforeCreate(dtoToCreate);

    // Validate that the DTO is not null
    if (dtoToCreate == null) {
      throw new IllegalArgumentException("DTO cannot be null for create operation");
    }

    // validate before update
    validateBeforeCreateOrUpdate(dtoToCreate);

    // Map DTO to Entity
    E entityToCreate = abstractMapper.map(dtoToCreate);

    // Pre-create hook
    preCreate(dtoToCreate, entityToCreate);

    // Save entity
    E createdEntity = abstractJpaRepository.saveAndFlush(entityToCreate);

    // Retrieve the created entity to ensure all fields are populated
    createdEntity = assertEntityByIdPost(createdEntity.getId());

    // Post-create hook
    postCreate(dtoToCreate, createdEntity);

    // Map back to DTO and return
    return abstractMapper.map(createdEntity);
  }

  // Hooks called to modify the DTO before creation
  protected void updateDtoBeforeCreate(D dtoToCreate) {}

  // Hook called before the entity is persisted
  protected void preCreate(D dtoToCreate, E entityToCreate) {}

  // Hook called after the entity is persisted
  protected void postCreate(D dtoToCreate, E createdEntity) {}

  // Do not call this method directly, use the template method instead
  public D update(D dto) {
    return ServiceOperationTemplate.executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoToUpdate) {
            if (!Boolean.TRUE.equals(dto.getIsAtomicOperation()))
              return self.executeUpdateTransactional(dtoToUpdate);
            else return executeUpdate(dtoToUpdate);
          }
        },
        dto);
  }

  // Execute update in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeUpdateTransactional(D dto) {
    return executeUpdate(dto);
  }

  // Execute update in the current transaction
  public D executeUpdateNestedTransactional(D dto) {
    return executeUpdate(dto);
  }

  // Core update logic
  private D executeUpdate(D dtoToUpdate) {
    // Hook for subclasses to modify dto before update
    updateDtoBeforeUpdate(dtoToUpdate);

    // Validate that the DTO has an ID
    if (dtoToUpdate == null || dtoToUpdate.getId() == null) {
      throw new IllegalArgumentException("DTO id cannot be null for update operation");
    }

    // validate before update
    validateBeforeCreateOrUpdate(dtoToUpdate);

    // Retrieve existing entity
    E existingEntity = assertEntityByIdPre(dtoToUpdate.getId());

    // Create a clone of the existing entity to apply updates
    @SuppressWarnings("unchecked")
    E existingEntityClone = (E) existingEntity.clone(); // Create a copy for pre-update

    // Apply updates from DTO to the cloned entity
    makeUpdates(dtoToUpdate, existingEntityClone);

    // Pre-update hook
    preUpdate(dtoToUpdate, existingEntity, existingEntityClone);

    // Save the updated entity
    E updatedEntity = abstractJpaRepository.saveAndFlush(existingEntityClone);

    // Retrieve the updated entity to ensure all fields are populated
    updatedEntity = assertEntityByIdPost(updatedEntity.getId());

    // Post-update hook
    postUpdate(dtoToUpdate, existingEntity, updatedEntity);

    // Map back to DTO and return
    return abstractMapper.map(updatedEntity);
  }

  // Hooks called to modify the DTO before update
  protected void updateDtoBeforeUpdate(D dtoToUpdate) {}

  // Hook to apply updates from DTO to the existing entity
  protected void makeUpdates(D dtoToUpdate, E existingEntity) {
    abstractMapper.mapDTOToEntity(dtoToUpdate, existingEntity);
  }

  // Hook called before the entity is updated
  protected void preUpdate(D dtoToUpdate, E entityBeforeUpdates, E updatedEntity) {}

  // Hook called after the entity is updated
  protected void postUpdate(D dtoToUpdate, E entityBeforeUpdate, E updatedEntity) {}

  // Hook to validate DTO before create or update
  protected void validateBeforeCreateOrUpdate(D dtoToCheck) {}

  // Do not call this method directly, use the template method instead
  public D delete(D dto) {
    return ServiceOperationTemplate.executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoToDelete) {
            if (!Boolean.TRUE.equals(dto.getIsAtomicOperation()))
              return self.executeDeleteTransactional(dtoToDelete);
            else return executeDelete(dtoToDelete);
          }
        },
        dto);
  }

  // Execute delete in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeDeleteTransactional(D dto) {
    return executeDelete(dto);
  }

  // Execute delete in the current transaction
  public D executeDeleteNestedTransactional(D dto) {
      return executeDelete(dto);
  }

  // Core delete logic
  private D executeDelete(D dtoToDelete) {
    // Hook for subclasses to modify dto before deletion
    updateDtoBeforeDelete(dtoToDelete);

    // Validate that the DTO has an ID
    if (dtoToDelete ==null || dtoToDelete.getId() == null) {
      throw new IllegalArgumentException("DTO id cannot be null for delete operation");
    }

    // Retrieve existing entity
    E existingEntity = assertEntityByIdPre(dtoToDelete.getId());

    // Pre-delete hook
    preDelete(dtoToDelete, existingEntity);

    // Delete the entity
    abstractJpaRepository.delete(existingEntity);
    abstractJpaRepository.flush();

    // Post-delete hook
    postDelete(dtoToDelete, existingEntity);

    // Return the original DTO
    return abstractMapper.map(existingEntity);
  }

  // Hooks called to modify the DTO before deletion
  protected void updateDtoBeforeDelete(D dtoToDelete) {}

  // Hook called before the entity is deleted
  protected void preDelete(D dtoToDelete, E entityToDelete) {}

  // Hook called after the entity is deleted
  protected void postDelete(D dtoToDelete, E deletedEntity) {}

  // Hook to mark the current transaction for rollback
  public void markRollback(D dto) {
    // Tell Spring to roll back this transaction without throwing
    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
  }

  // Get count of entity records matching the filter
  @Transactional(readOnly = true)
  public Long getCount(D filter) {
    return abstractDAO.getCount(filter, this::addEntityFilters);
  }

  // Fetch DTO records matching the filter
  private List<D> fetchDTORecords(D filter) {
    filter = updateFilterBeforeGet(filter);
    List<D> result = abstractDAO.get(filter, abstractMapper, this::addEntityFilters);
    return modifyGetResult(result, filter);
  }

  // Hook to modify the filter before fetching records
  protected D updateFilterBeforeGet(D filter) {
    return filter;
  }

  // Hook to add additional entity-level filters
  protected void addEntityFilters(CriteriaBuilderWrapper cbw, D filter) {}

  // Hook to modify the result list after fetching records
  protected List<D> modifyGetResult(List<D> result, D filter) {
    return result;
  }

  // Retrieve multiple DTOs matching the filter
  @Transactional(readOnly = true)
  public List<D> get(D dto) {
    return fetchDTORecords(dto);
  }

  // Retrieve a single DTO matching the filter, or null if none found
  @Transactional(readOnly = true)
  public D getSingle(D filter) {
    List<D> results = get(filter);
    return results.isEmpty() ? null : results.get(0);
  }

  // Retrieve a single DTO by its ID
  @Transactional(readOnly = true)
  public D getById(Long id) {
    try {
      D filter = dtoClass.getDeclaredConstructor().newInstance();
      filter.setId(id);
      return getSingle(filter);
    } catch (Exception e) {
      throw new RuntimeException("Could not create instance of DTO class", e);
    }
  }

  // Fetch entity records matching the filter
  private List<E> fetchEntityRecords(D filter) {
    return abstractDAO.get(filter, this::addEntityFilters);
  }

  // Retrieve multiple entities matching the filter
  @Transactional(readOnly = true)
  public List<E> getEntities(D filters) {
    return fetchEntityRecords(filters);
  }

  // Retrieve a single entity matching the filter, or null if none found
  @Transactional(readOnly = true)
  public E getSingleEntity(D filter) {
    List<E> results = getEntities(filter);
    return results.isEmpty() ? null : results.get(0);
  }

  // Retrieve a single entity by its ID
  @Transactional(readOnly = true)
  public E getEntityById(Long id) {
    try {
      D filter = dtoClass.getDeclaredConstructor().newInstance();
      filter.setId(id);
      return getSingleEntity(filter);
    } catch (Exception e) {
      throw new RuntimeException("Could not create instance of DTO class", e);
    }
  }

  // Asserts that an entity with the given ID exists, throws if not found
  // Uses pre-fetching via DAO to ensure filters and RBAC are applied
  private E assertEntityByIdPre(Long id) {
    try {
      D filter = dtoClass.getDeclaredConstructor().newInstance();
      filter.setId(id);
      E result = getSingleEntity(filter);
      if (result == null) throw new IllegalArgumentException("Entity with id " + id + " not found");
      return result;
    } catch (Exception e) {
      throw new RuntimeException("Could not create instance of DTO class", e);
    }
  }

  // Asserts that an entity with the given ID exists, throws if not found
  // Uses direct repository access to ensure the entity is freshly loaded
  private E assertEntityByIdPost(Long id) {
    return abstractJpaRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Entity with id " + id + " not found"));
  }
}
