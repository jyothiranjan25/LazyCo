package com.example.lazyco.core.AbstractClasses.Service;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.DAO.IAbstractDAO;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.core.Logger.ApplicationLogger;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.Getter;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Transactional
public abstract class AbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    implements IAbstractService<D, E> {

  // Cache for DTO class calculation to avoid repeated reflection
  private static final ConcurrentHashMap<Class<?>, Class<?>> dtoClassCache =
      new ConcurrentHashMap<>();

  @Lazy
  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private AbstractService<D, E> self;

  private final AbstractMapper<D, E> abstractMapper;
  private IAbstractDAO<D, E> abstractDAO;
  @Getter private final Class<D> dtoClass;

  protected AbstractService(AbstractMapper<D, E> abstractMapper) {
    this.abstractMapper = abstractMapper;
    dtoClass = this.calculateDTOClass();
  }

  @Autowired
  private void injectDependencies(IAbstractDAO<D, E> abstractDAO) {
    this.abstractDAO = abstractDAO;
  }

  @SuppressWarnings("unchecked")
  private Class<D> calculateDTOClass() {
    // Use cache to avoid repeated reflection
    return (Class<D>)
        dtoClassCache.computeIfAbsent(
            getClass(),
            clazz -> {
              Type superClass = clazz.getGenericSuperclass();
              if (superClass instanceof ParameterizedType parameterizedType) {
                Type type = parameterizedType.getActualTypeArguments()[0];
                if (type instanceof Class) {
                  return (Class<?>) type;
                } else if (type instanceof ParameterizedType) {
                  return (Class<?>) ((ParameterizedType) type).getRawType();
                }
              }
              throw new ExceptionWrapper("Unable to determine DTO class for: " + clazz.getName());
            });
  }

  // Do not call this method directly, use the template method instead
  public D create(D dto) {
    Function<D, D> atomicOperation = self::executeCreateNestedTransactional;
    Function<D, D> nonAtomicOperation = self::executeCreateNewTransactional;
    Function<D, D> operation = chooseOperation(dto, atomicOperation, nonAtomicOperation);
    return executeWithTemplate(dto, operation);
  }

  // Execute create in the current transaction
  public D executeCreateTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeCreate);
  }

  public D executeCreateTransactional(D dto, boolean immediateFlush) {
    return executeWithTemplate(dto, immediateFlush, this::executeCreate);
  }

  // Execute create in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeCreateNewTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeCreate);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeCreateNewTransactional(D dto, boolean immediateFlush) {
    return executeWithTemplate(dto, immediateFlush, this::executeCreate);
  }

  // Execute create in a nested transaction (saves a savepoint)
  @Transactional(propagation = Propagation.NESTED)
  public D executeCreateNestedTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeCreate);
  }

  @Transactional(propagation = Propagation.NESTED)
  public D executeCreateNestedTransactional(D dto, boolean immediateFlush) {
    return executeWithTemplate(dto, immediateFlush, this::executeCreate);
  }

  // Core create logic
  private D executeCreate(D dtoToCreate, boolean immediateFlush) {
    // Hook for subclasses to modify dto before creation
    updateDtoBeforeCreate(dtoToCreate);

    // Validate that the DTO is not null
    if (dtoToCreate == null) {
      throw new ApplicationException(CommonMessage.OBJECT_REQUIRED);
    }

    // validate before update
    validateBeforeCreate(dtoToCreate);

    // Map DTO to Entity
    E entityToCreate = abstractMapper.map(dtoToCreate);

    // Pre-create hook
    preCreate(dtoToCreate, entityToCreate);

    // Save entity
    E createdEntity;
    if (immediateFlush) {
      createdEntity = abstractDAO.saveAndFlush(entityToCreate);
    } else {
      createdEntity = abstractDAO.save(entityToCreate);
    }

    // Post-create hook
    postCreate(dtoToCreate, createdEntity);

    // Map back to DTO and return
    D createdDTO = abstractMapper.map(createdEntity);
    return modifyCreateResult(dtoToCreate, createdDTO);
  }

  // Hooks called to modify the DTO before creation
  protected void updateDtoBeforeCreate(D requestDTO) {}

  // Hook to validate DTO before create or update
  protected void validateBeforeCreate(D requestDTO) {}

  // Hook called before the entity is persisted
  protected void preCreate(D requestDTO, E entityToCreate) {}

  // Hook called after the entity is persisted
  protected void postCreate(D requestDTO, E createdEntity) {}

  // Hook to modify the created DTO before returning to caller
  protected D modifyCreateResult(D requestDTO, D createdDTO) {
    return createdDTO;
  }

  // Do not call this method directly, use the template method instead
  public D update(D dto) {
    Function<D, D> atomicOperation = self::executeUpdateNestedTransactional;
    Function<D, D> nonAtomicOperation = self::executeUpdateNewTransactional;
    Function<D, D> operation = chooseOperation(dto, atomicOperation, nonAtomicOperation);
    return executeWithTemplate(dto, operation);
  }

  // Execute update in the current transaction
  public D executeUpdateTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeUpdate);
  }

  public D executeUpdateTransactional(D dto, boolean immediateFlush) {
    return executeWithTemplate(dto, immediateFlush, this::executeUpdate);
  }

  // Execute update in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeUpdateNewTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeUpdate);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeUpdateNewTransactional(D dto, boolean immediateFlush) {
    return executeWithTemplate(dto, immediateFlush, this::executeUpdate);
  }

  // Execute update in a nested transaction (saves a savepoint)
  @Transactional(propagation = Propagation.NESTED)
  public D executeUpdateNestedTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeUpdate);
  }

  @Transactional(propagation = Propagation.NESTED)
  public D executeUpdateNestedTransactional(D dto, boolean immediateFlush) {
    return executeWithTemplate(dto, immediateFlush, this::executeUpdate);
  }

  // Core update logic
  private D executeUpdate(D dtoToUpdate, boolean immediateFlush) {
    // Hook for subclasses to modify dto before update
    updateDtoBeforeUpdate(dtoToUpdate);

    // Validate that the DTO has an ID
    if (Objects.isNull(dtoToUpdate) || Objects.isNull(dtoToUpdate.getId())) {
      throw new ApplicationException(CommonMessage.OBJECT_REQUIRED);
    }

    // validate before update
    validateBeforeUpdate(dtoToUpdate);

    // Retrieve existing entity
    E existingEntity = assertEntityByIdPre(dtoToUpdate.getId());

    // Create a clone of the existing entity to apply updates
    D entityClone = abstractMapper.map(existingEntity);

    // Map the cloned entity back to an entity instance to be used in hooks
    E entityCloneEntity = abstractMapper.map(entityClone);

    // Apply updates from DTO to the cloned entity
    makeUpdates(dtoToUpdate, existingEntity);

    makeUpdates(dtoToUpdate, entityCloneEntity, existingEntity);

    // Pre-update hook
    preUpdate(dtoToUpdate, entityClone, existingEntity);

    // Save the updated entity
    E updatedEntity;
    if (immediateFlush) {
      updatedEntity = abstractDAO.updateAndFlush(existingEntity);
    } else {
      updatedEntity = abstractDAO.update(existingEntity);
    }

    // Post-update hook
    postUpdate(dtoToUpdate, entityClone, updatedEntity);

    // Map back to DTO and return
    D updatedDTO = abstractMapper.map(updatedEntity);
    return modifyUpdateResult(dtoToUpdate, updatedDTO);
  }

  // Hooks called to modify the DTO before update
  protected void updateDtoBeforeUpdate(D dtoToUpdate) {}

  // Hook to validate DTO before create or update
  protected void validateBeforeUpdate(D requestDTO) {}

  // Hook to apply updates from DTO to the existing entity
  protected void makeUpdates(D source, E target) {
    abstractMapper.mapDTOToEntity(source, target);
  }

  // Overloaded hook to provide both the original entity and the cloned entity for updates
  protected void makeUpdates(D source, E beforeUpdates, E afterUpdates) {}

  // Hook called before the entity is updated
  protected void preUpdate(D requestDTO, D entityBeforeUpdates, E entityToUpdate) {}

  // Hook called after the entity is updated
  protected void postUpdate(D requestDTO, D entityBeforeUpdate, E updatedEntity) {}

  // Hook to modify the updated DTO before returning to caller
  protected D modifyUpdateResult(D requestDTO, D updatedDTO) {
    return updatedDTO;
  }

  // Do not call this method directly, use the template method instead
  public D delete(D dto) {
    Function<D, D> atomicOperation = self::executeDeleteNestedTransactional;
    Function<D, D> nonAtomicOperation = self::executeDeleteNewTransactional;
    Function<D, D> operation = chooseOperation(dto, atomicOperation, nonAtomicOperation);
    return executeWithTemplate(dto, operation);
  }

  // Execute delete in the current transaction
  public D executeDeleteTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeDelete);
  }

  // Execute delete in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeDeleteNewTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeDelete);
  }

  // Execute delete in a nested transaction (saves a savepoint)
  @Transactional(propagation = Propagation.NESTED)
  public D executeDeleteNestedTransactional(D dto) {
    return executeWithTemplate(dto, true, this::executeDelete);
  }

  // Core delete logic
  private D executeDelete(D dtoToDelete, boolean immediateFlush) {
    // Hook for subclasses to modify dto before deletion
    updateDtoBeforeDelete(dtoToDelete);

    // Validate that the DTO has an ID
    if (Objects.isNull(dtoToDelete) || Objects.isNull(dtoToDelete.getId())) {
      throw new ApplicationException(CommonMessage.OBJECT_REQUIRED);
    }

    // Retrieve existing entity
    E existingEntity = assertEntityByIdPre(dtoToDelete.getId());

    // Pre-delete hook
    preDelete(dtoToDelete, existingEntity);

    // Delete the entity
    if (immediateFlush) {
      existingEntity = abstractDAO.deleteAndFlush(existingEntity);
    } else {
      existingEntity = abstractDAO.delete(existingEntity);
    }

    // Post-delete hook
    postDelete(dtoToDelete, existingEntity);

    // Return the original DTO
    return abstractMapper.map(existingEntity);
  }

  // Hooks called to modify the DTO before deletion
  protected void updateDtoBeforeDelete(D requestDTO) {}

  // Hook called before the entity is deleted
  protected void preDelete(D requestDTO, E entityToDelete) {}

  // Hook called after the entity is deleted
  protected void postDelete(D requestDTO, E deletedEntity) {}

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

  @Transactional(readOnly = true)
  public List<D> search(D filter) {
    return fetchDTORecords(filter);
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
    if (id == null) {
      throw new ApplicationException(CommonMessage.ID_REQUIRED);
    }
    try {
      D criteria = createFilterDto();
      criteria.setId(id);
      return getSingle(criteria);
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      ApplicationLogger.error(e);
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Fetch entity records matching the filter
  private List<E> fetchEntityRecords(D filter) {
    List<E> result = abstractDAO.get(filter, this::addEntityFilters);
    return modifyEntityResult(result, filter);
  }

  // Hook to modify the entity result list after fetching records
  protected List<E> modifyEntityResult(List<E> result, D filter) {
    return result;
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
    if (id == null) {
      throw new ApplicationException(CommonMessage.ID_REQUIRED);
    }
    try {
      D criteria = createFilterDto();
      criteria.setId(id);
      return getSingleEntity(criteria);
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      ApplicationLogger.error(e);
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Utility method to create a new instance of the filter DTO
  private D createFilterDto() throws ReflectiveOperationException {
    return getDtoClass().getDeclaredConstructor().newInstance();
  }

  // Asserts that an entity with the given ID exists, throws if not found
  // Uses pre-fetching via DAO to ensure filters and RBAC are applied
  private E assertEntityByIdPre(Long id) {
    if (id == null) {
      throw new ApplicationException(CommonMessage.ID_REQUIRED);
    }
    try {
      D criteria = createFilterDto();
      criteria.setId(id);
      E result = getSingleEntity(criteria);
      if (result == null)
        throw new ApplicationException(HttpStatus.NOT_FOUND, CommonMessage.OBJECT_NOT_FOUND);
      return result;
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      ApplicationLogger.error(e);
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Asserts that an entity with the given ID exists, throws if not found
  // Uses direct repository access to ensure the entity is freshly loaded
  private E assertEntityByIdPost(Class<E> clazz, Long id) {
    return abstractDAO.findById(clazz, id);
  }

  // Template method to choose between atomic and non-atomic operations based on the DTO flag
  private Function<D, D> chooseOperation(D dto, Function<D, D> atomic, Function<D, D> nonAtomic) {
    return Boolean.TRUE.equals(dto.getIsAtomicOperation()) ? atomic : nonAtomic;
  }

  // Template method to execute the given operation with the service operation template
  private D executeWithTemplate(D dto, Function<D, D> operation) {
    return ServiceOperationTemplate.executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoInput) {
            return operation.apply(dtoInput);
          }
        },
        dto);
  }

  private D executeWithTemplate(
      D dto, boolean immediateFlush, BiFunction<D, Boolean, D> operation) {
    return ServiceOperationTemplate.executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoInput) {
            return operation.apply(dtoInput, immediateFlush);
          }
        },
        dto);
  }

  // Hook to mark the current transaction for rollback without throwing an exception
  public void markRollback(D dto) {
    ApplicationLogger.warn(
        "Transaction marked for rollback for DTO: {}", dto.getClass().getSimpleName());
    // Tell Spring to roll back this transaction without throwing
    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
  }
}
