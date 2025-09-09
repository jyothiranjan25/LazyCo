package com.example.lazyco.backend.core.AbstractDocClasses.Service;

import com.example.lazyco.backend.core.AbstractDocClasses.DAO.IAbstractDocDAO;
import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractDocClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.Exceptions.ApplicationExemption;
import com.example.lazyco.backend.core.Exceptions.CommonMessage;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Exceptions.ResourceNotFoundExemption;
import com.example.lazyco.backend.core.MongoCriteriaBuilder.MongoCriteriaBuilderWrapper;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Transactional("mongoTransactionManager")
public abstract class AbstractService<D extends AbstractDTO<D>, E extends AbstractModel>
    implements IAbstractService<D, E> {

  @Lazy
  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private AbstractService<D, E> self;

  private final AbstractMapper<D, E> abstractMapper;
  private IAbstractDocDAO<D, E> abstractDAO;
  @Getter private final Class<D> dtoClass;

  protected AbstractService(AbstractMapper<D, E> abstractMapper) {
    this.abstractMapper = abstractMapper;
    dtoClass = this.calculateDTOClass();
  }

  @Autowired
  private void injectDependencies(IAbstractDocDAO<D, E> abstractDAO) {
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
    throw new ExceptionWrapper("Unable to determine DTO class");
  }

  // Do not call this method directly, use the template method instead
  public D create(D dto) {
    return executeWithTemplate(
        dto, self::executeCreateTransactional, self::executeCreateNewTransactional);
  }

  // Execute create in the current transaction
  public D executeCreateTransactional(D dto) {
    return executeCreate(dto);
  }

  // Execute create in a new transaction
  @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRES_NEW)
  public D executeCreateNewTransactional(D dto) {
    return executeCreate(dto);
  }

  // Core create logic
  private D executeCreate(D dtoToCreate) {
    // Hook for subclasses to modify dto before creation
    updateDtoBeforeCreate(dtoToCreate);

    // Validate that the DTO is not null
    if (dtoToCreate == null) {
      throw new ApplicationExemption(CommonMessage.OBJECT_REQUIRED);
    }

    // validate before update
    validateBeforeCreateOrUpdate(dtoToCreate);

    // Map DTO to Entity
    E entityToCreate = abstractMapper.map(dtoToCreate);

    // Pre-create hook
    preCreate(dtoToCreate, entityToCreate);

    // Save entity
    entityToCreate = abstractDAO.save(entityToCreate);

    // Retrieve the created entity to ensure all fields are populated
    @SuppressWarnings("unchecked")
    E createdEntity =
        assertEntityByIdPost((Class<E>) entityToCreate.getClass(), entityToCreate.getId());

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
    return self.executeWithTemplate(
        dto, self::executeUpdateTransactional, self::executeUpdateNewTransactional);
  }

  // Execute update in the current transaction
  public D executeUpdateTransactional(D dto) {
    return executeUpdate(dto);
  }

  // Execute update in a new transaction
  @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRES_NEW)
  public D executeUpdateNewTransactional(D dto) {
    return executeUpdate(dto);
  }

  // Core update logic
  private D executeUpdate(D dtoToUpdate) {
    // Hook for subclasses to modify dto before update
    updateDtoBeforeUpdate(dtoToUpdate);

    // Validate that the DTO has an ID
    if (dtoToUpdate == null || dtoToUpdate.getId() == null) {
      throw new ApplicationExemption(CommonMessage.OBJECT_REQUIRED);
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

    // Retrieve the updated entity to ensure all fields are populated
    @SuppressWarnings("unchecked")
    E updatedEntityClean =
        assertEntityByIdPost((Class<E>) updatedEntity.getClass(), updatedEntity.getId());

    // Post-update hook
    postUpdate(dtoToUpdate, existingEntity, updatedEntityClean);

    // Map back to DTO and return
    return abstractMapper.map(updatedEntityClean);
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
    return self.executeWithTemplate(
        dto, self::executeDeleteTransactional, self::executeDeleteNewTransactional);
  }

  // Execute delete in the current transaction
  public D executeDeleteTransactional(D dto) {
    return executeDelete(dto);
  }

  // Execute delete in a new transaction
  @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRES_NEW)
  public D executeDeleteNewTransactional(D dto) {
    return executeDelete(dto);
  }

  // Core delete logic
  private D executeDelete(D dtoToDelete) {
    // Hook for subclasses to modify dto before deletion
    updateDtoBeforeDelete(dtoToDelete);

    // Validate that the DTO has an ID
    if (dtoToDelete == null || dtoToDelete.getId() == null) {
      throw new ApplicationExemption(CommonMessage.OBJECT_REQUIRED);
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
  protected void updateDtoBeforeDelete(D dtoToDelete) {}

  // Hook called before the entity is deleted
  protected void preDelete(D dtoToDelete, E entityToDelete) {}

  // Hook called after the entity is deleted
  protected void postDelete(D dtoToDelete, E deletedEntity) {}

  // Get count of entity records matching the filter
  @Transactional(value = "mongoTransactionManager",readOnly = true)
  public Long getCount(D filter) {
    return abstractDAO.getCount(filter, this::addEntityFilters);
  }

  // Fetch DTO records matching the filter
  private List<D> fetchDTORecords(
      D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> additionalFilters) {
    filter = updateFilterBeforeGet(filter);
    List<D> result = abstractDAO.get(filter, abstractMapper, additionalFilters);
    return modifyGetResult(result, filter);
  }

  // Hook to modify the filter before fetching records
  protected D updateFilterBeforeGet(D filter) {
    return filter;
  }

  // Hook to add additional entity-level filters
  protected void addEntityFilters(MongoCriteriaBuilderWrapper cbw, D filter) {}

  // Hook to modify the result list after fetching records
  protected List<D> modifyGetResult(List<D> result, D filter) {
    return result;
  }

  // Retrieve multiple DTOs matching the filter
  @Transactional(value = "mongoTransactionManager", readOnly = true)
  public List<D> get(D dto) {
    return fetchDTORecords(dto, this::addEntityFilters);
  }

  // Retrieve multiple DTOs matching the filter
  @Transactional(value = "mongoTransactionManager", readOnly = true)
  public List<D> get(D dto, BiConsumer<MongoCriteriaBuilderWrapper, D> additionalFilters) {
    return fetchDTORecords(dto, additionalFilters);
  }

  // Retrieve a single DTO matching the filter, or null if none found
  @Transactional(value = "mongoTransactionManager", readOnly = true)
  public D getSingle(D filter) {
    List<D> results = get(filter);
    return results.isEmpty() ? null : results.get(0);
  }

  // Retrieve a single DTO by its ID
  @Transactional(value = "mongoTransactionManager", readOnly = true)
  public D getById(String id) {
    if (id == null) {
      throw new ApplicationExemption(CommonMessage.ID_REQUIRED);
    }
    try {
      D criteria = createFilterDto();
      criteria.setId(id);
      return getSingle(criteria);
    } catch (ApplicationExemption e) {
      throw e;
    } catch (Exception e) {
      throw new ApplicationExemption(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Fetch entity records matching the filter
  private List<E> fetchEntityRecords(
      D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> additionalFilters) {
    return abstractDAO.get(filter, additionalFilters);
  }

  // Retrieve multiple entities matching the filter
  @Transactional(value = "mongoTransactionManager", readOnly = true)
  public List<E> getEntities(D filters) {
    return fetchEntityRecords(filters, this::addEntityFilters);
  }

  @Transactional(value = "mongoTransactionManager", readOnly = true)
  public List<E> getEntities(
      D filters, BiConsumer<MongoCriteriaBuilderWrapper, D> additionalFilters) {
    return fetchEntityRecords(filters, additionalFilters);
  }

  // Retrieve a single entity matching the filter, or null if none found
  @Transactional(value = "mongoTransactionManager", readOnly = true)
  public E getSingleEntity(D filter) {
    List<E> results = getEntities(filter);
    return results.isEmpty() ? null : results.get(0);
  }

  // Retrieve a single entity by its ID
  @Transactional(value = "mongoTransactionManager", readOnly = true)
  public E getEntityById(String id) {
    if (id == null) {
      throw new ApplicationExemption(CommonMessage.ID_REQUIRED);
    }
    try {
      D criteria = createFilterDto();
      criteria.setId(id);
      return getSingleEntity(criteria);
    } catch (ApplicationExemption e) {
      throw e;
    } catch (Exception e) {
      throw new ApplicationExemption(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Utility method to create a new instance of the filter DTO
  private D createFilterDto() throws ReflectiveOperationException {
    return dtoClass.getDeclaredConstructor().newInstance();
  }

  // Asserts that an entity with the given ID exists, throws if not found
  // Uses pre-fetching via DAO to ensure filters and RBAC are applied
  private E assertEntityByIdPre(String id) {
    if (id == null) {
      throw new ApplicationExemption(CommonMessage.ID_REQUIRED);
    }
    try {
      D criteria = createFilterDto();
      criteria.setId(id);
      E result = getSingleEntity(criteria);
      if (result == null) throw new ResourceNotFoundExemption(CommonMessage.OBJECT_NOT_FOUND);
      return result;
    } catch (ApplicationExemption | ResourceNotFoundExemption e) {
      throw e;
    } catch (Exception e) {
      throw new ApplicationExemption(CommonMessage.APPLICATION_ERROR);
    }
  }

  // Asserts that an entity with the given ID exists, throws if not found
  // Uses direct repository access to ensure the entity is freshly loaded
  private E assertEntityByIdPost(Class<E> clazz, String id) {
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
