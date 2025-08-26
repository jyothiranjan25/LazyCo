package com.example.lazyco.backend.core.AbstractClasses.Service;

import static com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents.ServiceOperationTemplate.executeServiceOperationTemplate;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModelBase;
import com.example.lazyco.backend.core.AbstractClasses.JpaRepository.AbstractJpaRepository;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents.ServiceOperationTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
@Transactional
public abstract class AbstractService<D extends AbstractDTO<D>, E extends AbstractModelBase>
    implements IAbstractService<D> {

  private final AbstractMapper<D, E> abstractMapper;
  private final AbstractJpaRepository<E> abstractJpaRepository;

  protected AbstractService(
      AbstractMapper<D, E> abstractMapper, AbstractJpaRepository<E> abstractJpaRepository) {
    this.abstractMapper = abstractMapper;
    this.abstractJpaRepository = abstractJpaRepository;
  }

  private E getEntityById(Long id) {
    return abstractJpaRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Entity with id " + id + " not found"));
  }

  // Do not call this method directly, use the template method instead
  @Override
  public D create(D dto) {
    return executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoToCreate) {
            return executeCreate(dtoToCreate);
          }
        },
        dto);
  }

  private D executeCreate(D dtoToCreate) {
    // Hook for subclasses to modify dto before creation
    updateDtoBeforeCreate(dtoToCreate);

    // validate before update
    validateBeforeCreateOrUpdate(dtoToCreate);

    // Map DTO to Entity
    E entityToCreate = abstractMapper.map(dtoToCreate);

    // Pre-create hook
    preCreate(dtoToCreate, entityToCreate);

    // Save entity
    E createdEntity = abstractJpaRepository.saveAndFlush(entityToCreate);

    // Retrieve the created entity to ensure all fields are populated
    createdEntity = getEntityById(createdEntity.getId());

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
  @Override
  public D update(D dto) {
    return executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoToUpdate) {
            return executeUpdate(dtoToUpdate);
          }
        },
        dto);
  }

  private D executeUpdate(D dtoToUpdate) {
    // Hook for subclasses to modify dto before update
    updateDtoBeforeUpdate(dtoToUpdate);

    // Validate that the DTO has an ID
    if (dtoToUpdate.getId() == null) {
      throw new IllegalArgumentException("DTO id cannot be null for update operation");
    }

    // validate before update
    validateBeforeCreateOrUpdate(dtoToUpdate);

    // Retrieve existing entity
    E existingEntity = getEntityById(dtoToUpdate.getId());

    // Create a clone of the existing entity to apply updates
    E existingEntityClone = (E) existingEntity.clone(); // Create a copy for pre-update

    // Apply updates from DTO to the cloned entity
    makeUpdates(dtoToUpdate, existingEntityClone);

    // Pre-update hook
    preUpdate(dtoToUpdate, existingEntity, existingEntityClone);

    // Save the updated entity
    E updatedEntity = abstractJpaRepository.saveAndFlush(existingEntityClone);

    // Retrieve the updated entity to ensure all fields are populated
    updatedEntity = getEntityById(updatedEntity.getId());

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
  protected abstract void validateBeforeCreateOrUpdate(D dtoToCheck);

  // Do not call this method directly, use the template method instead
  @Override
  public D delete(D dto) {
    return executeServiceOperationTemplate(
        new ServiceOperationTemplate<D>(this) {
          @Override
          public D execute(D dtoToDelete) {
            return executeDelete(dtoToDelete);
          }
        },
        dto);
  }

  private D executeDelete(D dtoToDelete) {
    // Hook for subclasses to modify dto before deletion
    updateDtoBeforeDelete(dtoToDelete);

    // Validate that the DTO has an ID
    if (dtoToDelete.getId() == null) {
      throw new IllegalArgumentException("DTO id cannot be null for delete operation");
    }

    // Retrieve existing entity
    E existingEntity = getEntityById(dtoToDelete.getId());

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


    @Override
    public D getSingle(D filter) {
        return null;
    }

    @Override
    public D getById(Long id) {
        return null;
    }

    @Override
    public Long getCount(D filter) {
        return 0L;
    }

    @Override
    public List<D> get(D dto) {
      E entity = abstractMapper.map(dto);
      D mappedDto = abstractMapper.map(entity);
      return List.of(mappedDto);
    }
}
