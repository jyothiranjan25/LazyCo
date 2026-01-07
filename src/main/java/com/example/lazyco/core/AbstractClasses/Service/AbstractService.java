package com.example.lazyco.core.AbstractClasses.Service;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.DAO.IAbstractDAO;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.core.Messages.CustomMessage;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    return executeWithTemplate(
        dto, self::executeCreateNestedTransactional, self::executeCreateNewTransactional);
  }

  // Execute create in the current transaction
  public D executeCreateTransactional(D dto) {
    return executeCreate(dto);
  }

  // Execute create in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeCreateNewTransactional(D dto) {
    return executeCreate(dto);
  }

  // Execute create in a nested transaction (saves a savepoint)
  @Transactional(propagation = Propagation.NESTED)
  public D executeCreateNestedTransactional(D dto) {
    return executeCreate(dto);
  }

  // Core create logic
  private D executeCreate(D dtoToCreate) {
    // Hook for subclasses to modify dto before creation
    updateDtoBeforeCreate(dtoToCreate);

    // Validate that the DTO is not null
    if (dtoToCreate == null) {
      throw new ApplicationException(CommonMessage.OBJECT_REQUIRED);
    }

    // validate before update
    validateBeforeCreateOrUpdate(dtoToCreate);

    // Map DTO to Entity
    E entityToCreate = abstractMapper.map(dtoToCreate);

    // Pre-create hook
    preCreate(dtoToCreate, entityToCreate);

    // Save entity
    E createdEntity = abstractDAO.save(entityToCreate);

    // TODO: (low priority) optimize to avoid double DB hit
    // Retrieve the created entity to ensure all fields are populated
    @SuppressWarnings("unchecked")
    E refreshedEntity =
        assertEntityByIdPost((Class<E>) entityToCreate.getClass(), createdEntity.getId());

    // Post-create hook
    postCreate(dtoToCreate, refreshedEntity);

    // Map back to DTO and return
    return abstractMapper.map(refreshedEntity);
  }

  // Hooks called to modify the DTO before creation
  protected void updateDtoBeforeCreate(D requestDTO) {}

  // Hook called before the entity is persisted
  protected void preCreate(D requestDTO, E entityToCreate) {}

  // Hook called after the entity is persisted
  protected void postCreate(D requestDTO, E createdEntity) {}

  // Do not call this method directly, use the template method instead
  public D update(D dto) {
    return executeWithTemplate(
        dto, self::executeUpdateNestedTransactional, self::executeUpdateNewTransactional);
  }

  // Execute update in the current transaction
  public D executeUpdateTransactional(D dto) {
    return executeUpdate(dto);
  }

  // Execute update in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeUpdateNewTransactional(D dto) {
    return executeUpdate(dto);
  }

  // Execute update in a nested transaction (saves a savepoint)
  @Transactional(propagation = Propagation.NESTED)
  public D executeUpdateNestedTransactional(D dto) {
    return executeUpdate(dto);
  }

  // Core update logic
  private D executeUpdate(D dtoToUpdate) {
    // Hook for subclasses to modify dto before update
    updateDtoBeforeUpdate(dtoToUpdate);

    // Validate that the DTO has an ID
    if (Objects.isNull(dtoToUpdate) || Objects.isNull(dtoToUpdate.getId())) {
      throw new ApplicationException(CommonMessage.OBJECT_REQUIRED);
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
    E updatedEntity = abstractDAO.update(existingEntityClone);

    // TODO: (low priority) optimize to avoid double DB hit
    // Retrieve the updated entity to ensure all fields are populated
    @SuppressWarnings("unchecked")
    E refreshedEntity =
        assertEntityByIdPost((Class<E>) updatedEntity.getClass(), updatedEntity.getId());

    // Post-update hook
    postUpdate(dtoToUpdate, existingEntity, refreshedEntity);

    // Map back to DTO and return
    return abstractMapper.map(refreshedEntity);
  }

  // Hooks called to modify the DTO before update
  protected void updateDtoBeforeUpdate(D dtoToUpdate) {}

  // Hook to apply updates from DTO to the existing entity
  protected void makeUpdates(D source, E target) {
    abstractMapper.mapDTOToEntity(source, target);
  }

  // Hook called before the entity is updated
  protected void preUpdate(D requestDTO, E entityBeforeUpdates, E entityAfterUpdates) {}

  // Hook called after the entity is updated
  protected void postUpdate(D requestDTO, E entityBeforeUpdate, E updatedEntity) {}

  // Hook to validate DTO before create or update
  protected void validateBeforeCreateOrUpdate(D requestDTO) {}

  // Do not call this method directly, use the template method instead
  public D delete(D dto) {
    return executeWithTemplate(
        dto, self::executeDeleteNestedTransactional, self::executeDeleteNewTransactional);
  }

  // Execute delete in the current transaction
  public D executeDeleteTransactional(D dto) {
    return executeDelete(dto);
  }

  // Execute delete in a new transaction
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public D executeDeleteNewTransactional(D dto) {
    return executeDelete(dto);
  }

  // Execute delete in a nested transaction (saves a savepoint)
  @Transactional(propagation = Propagation.NESTED)
  public D executeDeleteNestedTransactional(D dto) {
    return executeDelete(dto);
  }

  // Core delete logic
  private D executeDelete(D dtoToDelete) {
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
    existingEntity = abstractDAO.delete(existingEntity);

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
    // Clone each DTO to avoid side effects
    @SuppressWarnings("unchecked")
    List<D> clonedResult = result.stream().map(dto -> (D) dto.clone()).toList();
    return modifyGetResult(clonedResult, filter);
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
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Utility method to create a new instance of the filter DTO
  private D createFilterDto() throws ReflectiveOperationException {
    return dtoClass.getDeclaredConstructor().newInstance();
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
        throw new EntityNotFoundException(
            CustomMessage.getMessageString(CommonMessage.OBJECT_NOT_FOUND));
      return result;
    } catch (ApplicationException | EntityNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ApplicationException(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Asserts that an entity with the given ID exists, throws if not found
  // Uses direct repository access to ensure the entity is freshly loaded
  private E assertEntityByIdPost(Class<E> clazz, Long id) {
    return abstractDAO.findById(clazz, id);
  }

  // Template method to execute operations with choice of atomic or non-atomic execution
  private D executeWithTemplate(
      D dto, Function<D, D> atomicOperation, Function<D, D> nonAtomicOperation) {
    return ServiceOperationTemplate.executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoInput) {
            Function<D, D> operation =
                Boolean.TRUE.equals(dto.getIsAtomicOperation())
                    ? atomicOperation
                    : nonAtomicOperation;
            return operation.apply(dtoInput);
          }
        },
        dto);
  }

  // Hook to mark the current transaction for rollback without throwing an exception
  public void markRollback(D dto) {
    // Tell Spring to roll back this transaction without throwing
    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
  }
}
